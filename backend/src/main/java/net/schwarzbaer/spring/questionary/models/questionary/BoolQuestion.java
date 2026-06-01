package net.schwarzbaer.spring.questionary.models.questionary;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.PolymorphicValue;
import net.schwarzbaer.spring.questionary.models.definitions.BoolQuestionDef;
import net.schwarzbaer.spring.questionary.models.page.BoolPage;

public class BoolQuestion extends Question<BoolQuestionDef>
{
    public BoolQuestion(QuestionGroup parentGroup, @NonNull QuestionIndex index, @NonNull BoolQuestionDef definition)
    {
        super(parentGroup, index, definition);
    }

    @Override
    public BoolPage createPage(boolean isFirst)
    {
        return new BoolPage(id, text, isFirst);
    }

    @Override
    public boolean meetsToValue(PolymorphicValue value)
    {
        return value instanceof PolymorphicValue.BoolValue;
    }
}
