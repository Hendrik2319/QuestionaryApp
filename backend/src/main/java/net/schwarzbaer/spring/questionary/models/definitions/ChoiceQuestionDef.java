package net.schwarzbaer.spring.questionary.models.definitions;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import net.schwarzbaer.spring.questionary.models.PolymorphicValue;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.resume.ChoiceQuestionDefDTO.MultipleDTO;
import net.schwarzbaer.spring.questionary.models.resume.ChoiceQuestionDefDTO.SingleDTO;

@Setter @Getter @ToString(callSuper=true)
public abstract class ChoiceQuestionDef extends QuestionDef
{
    private List<OptionDef> options;

    protected ChoiceQuestionDef(@NonNull SelectionType selectionType)
    {
        super(selectionType);
    }

    protected @NonNull List<OptionDef> buildAnswerList(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        Set<PolymorphicValue> answers = questionaryAnswers.answers().get(getId());
        return options==null || options.isEmpty() || answers==null || answers.isEmpty()
            ? List.of()
            : options
                .stream()
                .filter(opt -> answers.contains(new PolymorphicValue.StringValue(opt.value())))
                .map(OptionDef::createCopy)
                .toList();
    }

    @Setter @Getter @ToString(callSuper=true)
    public static class Multiple extends ChoiceQuestionDef
    {
        public Multiple()
        {
            super(SelectionType.Multiple);
        }

        @Override
        public MultipleDTO createDTOForResume(@NonNull QuestionaryAnswers questionaryAnswers)
        {
            return new MultipleDTO(getId(), getText(), buildAnswerList(questionaryAnswers));
        }
    }
    
    @Setter @Getter @ToString(callSuper=true)
    public static class Single extends ChoiceQuestionDef
    {
        public Single()
        {
            super(SelectionType.Single);
        }

        @Override
        public SingleDTO createDTOForResume(@NonNull QuestionaryAnswers questionaryAnswers)
        {
            return new SingleDTO(getId(), getText(), buildAnswerList(questionaryAnswers));
        }
    }
}
