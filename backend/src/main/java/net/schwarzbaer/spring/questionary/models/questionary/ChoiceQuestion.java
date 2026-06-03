package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.PolymorphicValue;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.definitions.ChoiceQuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.OptionDef;
import net.schwarzbaer.spring.questionary.models.definitions.SelectionType;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;
import net.schwarzbaer.spring.questionary.models.page.ChoicePage;
import net.schwarzbaer.spring.questionary.models.resume.ChoiceQuestionResumeDTO;

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
    public @NonNull ChoicePage createPage(boolean isFirst)
    {
        SelectionType selectionType = definition.getSelectionType();
        if (selectionType==null)
            throw new IllegalStateException("ChoiceQuestionDef.selectionType must not be NULL");

        List<String> texts = getTexts();
        List<OptionDef> options = definition.getOptions();
        switch (selectionType)
        {
        case Multiple: return new ChoicePage.Multiple(id, texts, isFirst, options);
        case Single  : return new ChoicePage.Single  (id, texts, isFirst, options);
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

    @Override
    public @NonNull ChoiceQuestionResumeDTO createResumeDTO(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        SelectionType selectionType = definition.getSelectionType();
        if (selectionType==null)
            throw new IllegalStateException("ChoiceQuestionDef.selectionType must not be NULL");

        @NonNull
        List<OptionDef> answerList = buildAnswerList(questionaryAnswers);
        boolean active = isActive(questionaryAnswers);
        switch (selectionType)
        {
        case Multiple: return new ChoiceQuestionResumeDTO.MultipleDTO(id, text, active, answerList);
        case Single  : return new ChoiceQuestionResumeDTO.SingleDTO  (id, text, active, answerList);
        default: throw new IllegalStateException("SelectionType has an unexpected enum value: %s".formatted(selectionType));
        }
    }


    private @NonNull List<OptionDef> buildAnswerList(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        Set<PolymorphicValue> answers = questionaryAnswers.answers().get(id);
        List<OptionDef> options = definition.getOptions();
        return options==null || options.isEmpty() || answers==null || answers.isEmpty()
            ? List.of()
            : options
                .stream()
                .filter(opt -> answers.contains(new PolymorphicValue.StringValue(opt.value())))
                .map(OptionDef::createCopy)
                .toList();
    }
}
