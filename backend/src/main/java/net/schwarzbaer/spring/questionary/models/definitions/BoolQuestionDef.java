package net.schwarzbaer.spring.questionary.models.definitions;

import java.util.Set;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import net.schwarzbaer.spring.questionary.models.PolymorphicValue;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.resume.BoolQuestionResumeDTO;

@Setter @Getter @ToString(callSuper=true)
public class BoolQuestionDef extends QuestionDef
{
    public BoolQuestionDef()
    {
        super(SelectionType.Single);
    }
    
    @Override
    public BoolQuestionResumeDTO createResumeDTO(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        return new BoolQuestionResumeDTO(
            getId(),
            getText(),
            getAnswer(
                questionaryAnswers
                    .answers()
                    .get(getId())
            )
        );
    }

    private Boolean getAnswer(Set<PolymorphicValue> answers)
    {
        if (answers==null || answers.isEmpty())
            return null;
        if (answers.contains(new PolymorphicValue.BoolValue(true)))
            return true;
        if (answers.contains(new PolymorphicValue.BoolValue(false)))
            return false;
        return null;
    }
}
