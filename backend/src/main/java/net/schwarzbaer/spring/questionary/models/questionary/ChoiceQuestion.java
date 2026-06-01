package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.PolymorphicValue;
import net.schwarzbaer.spring.questionary.models.definitions.ChoiceQuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.OptionDef;
import net.schwarzbaer.spring.questionary.models.definitions.SelectionType;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;
import net.schwarzbaer.spring.questionary.models.page.ChoicePage;

public class ChoiceQuestion extends Question<ChoiceQuestionDef>
{
    public ChoiceQuestion(QuestionGroup parentGroup, @NonNull QuestionIndex index, @NonNull ChoiceQuestionDef definition)
    {
        super(parentGroup, index, definition);
    }

    @Override
    void checkDefinitionStructure() throws WrongDefinitionStructureException
    {
        super.checkDefinitionStructure();

        List<OptionDef> optionDefs = definition.getOptions();

        if (optionDefs==null)
            throw new WrongDefinitionStructureException(
                "ChoiceQuestion (id:\"%s\") has no field \"options\""
                .formatted(id)
            );

        if (optionDefs.isEmpty())
            throw new WrongDefinitionStructureException(
                "ChoiceQuestion (id:\"%s\") has an empty array in field \"options\""
                .formatted(id)
            );

        Set<String> values = new HashSet<>();
        for (OptionDef optionDef : optionDefs)
        {
            @NonNull
            String optionValue = optionDef.value();

            if (optionValue.isBlank())
                throw new WrongDefinitionStructureException(
                    "ChoiceQuestion (id:\"%s\") has an option with a blank value"
                    .formatted(id, optionDef.value())
                );

            if (!values.add(optionValue))
                throw new WrongDefinitionStructureException(
                    "ChoiceQuestion (id:\"%s\") has more than one option with same value \"%s\""
                    .formatted(id, optionDef.value())
                );
        }
    }

    @Override
    public ChoicePage createPage(boolean isFirst)
    {
        SelectionType selectionType = definition.getSelectionType();
        if (selectionType==null)
            throw new IllegalStateException("ChoiceQuestionDef.selectionType must not be NULL");

        switch (selectionType)
        {
        case Multiple: return new ChoicePage.Multiple(id, text, isFirst, definition.getOptions());
        case Single  : return new ChoicePage.Single  (id, text, isFirst, definition.getOptions());
        default: throw new IllegalStateException("SelectionType has an unexpected enum value: %s".formatted(selectionType));
        }
    }

    @Override
    public boolean meetsToValue(PolymorphicValue value)
    {
        if (value instanceof PolymorphicValue.StringValue stringValue)
            return isAnOptionValue(stringValue.value());
        return false;
    }

    private boolean isAnOptionValue(@NonNull String value)
    {
        List<OptionDef> optionDefs = definition.getOptions();
        for (OptionDef optionDef : optionDefs)
            if (value.equals(optionDef.value()))
                return true;
        return false;
    }
}
