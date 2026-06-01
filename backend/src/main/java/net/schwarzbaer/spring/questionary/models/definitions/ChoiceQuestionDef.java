package net.schwarzbaer.spring.questionary.models.definitions;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
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

    protected @NonNull List<OptionDef> getCopyOfOptions()
    {
        return options==null
            ? List.of()
            : options
                .stream()
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
        public MultipleDTO createDTOForResume()
        {
            return new MultipleDTO(getId(), getText(), getCopyOfOptions());
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
        public SingleDTO createDTOForResume()
        {
            return new SingleDTO(getId(), getText(), getCopyOfOptions());
        }
    }
}
