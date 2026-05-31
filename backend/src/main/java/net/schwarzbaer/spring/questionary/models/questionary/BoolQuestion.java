package net.schwarzbaer.spring.questionary.models.questionary;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.definitions.BoolQuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionValueDef;

public class BoolQuestion extends Question<BoolQuestionDef>
{
    public BoolQuestion(QuestionGroup parentGroup, @NonNull QuestionIndex index, @NonNull BoolQuestionDef definition)
    {
        super(parentGroup, index, definition);
    }

    @Override
    boolean meetToConditionValue(ConditionValueDef value)
    {
        return value instanceof ConditionValueDef.BoolValueDef;
    }
}
