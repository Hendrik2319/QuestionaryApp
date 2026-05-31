package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.List;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.definitions.ChoiceQuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionValueDef;
import net.schwarzbaer.spring.questionary.models.definitions.OptionDef;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;

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
        
        // for (OptionDef optionDef : optionDefs)
        // {
        //     optionDef
        // }
    }

    @Override
    boolean meetToConditionValue(ConditionValueDef value)
    {
        return value instanceof ConditionValueDef.StringValueDef;
    }

}
