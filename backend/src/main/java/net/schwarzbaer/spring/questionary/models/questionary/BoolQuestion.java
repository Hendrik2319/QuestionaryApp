package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.Set;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.PolymorphicValue;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.definitions.BoolQuestionDef;
import net.schwarzbaer.spring.questionary.models.page.BoolPage;
import net.schwarzbaer.spring.questionary.models.resume.BoolQuestionResumeDTO;

public class BoolQuestion extends Question<BoolQuestionDef>
{
    public BoolQuestion(QuestionGroup parentGroup, @NonNull QuestionIndex index, @NonNull BoolQuestionDef definition)
    {
        super(parentGroup, index, definition);
    }

    @Override
    public @NonNull BoolPage createPage(boolean isFirst)
    {
        return new BoolPage(id, getTexts(), isFirst);
    }

    @Override
    public boolean meetsToValue(PolymorphicValue value)
    {
        return value instanceof PolymorphicValue.BoolValue;
    }

    @Override
    public @NonNull BoolQuestionResumeDTO createResumeDTO(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        return new BoolQuestionResumeDTO(
                id, text,
                isActive(questionaryAnswers),
                getAnswer(
                    questionaryAnswers
                        .answers()
                        .get(id)
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
