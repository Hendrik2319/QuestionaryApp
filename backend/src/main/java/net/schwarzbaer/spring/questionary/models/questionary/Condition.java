package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.Set;
import java.util.function.Function;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionDef;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;

@RequiredArgsConstructor
public class Condition
{
    @NonNull private final ConditionDef definition;
    private Question<?> referredQuestion = null;

    void dereferenceQuestionId(@NonNull Function<String,Question<?>> findQuestion)
    {
        referredQuestion = findQuestion.apply(definition.questionId());
    }

    void checkDefinitionStructure(@NonNull Question<?> parentQuestion) throws WrongDefinitionStructureException
    {
        String referredQuestionId = definition.questionId();

        if (referredQuestion==null)
    		throw new WrongDefinitionStructureException(
                "A condition of question (id:\"%s\") is referring (id:\"%s\") to unknown question"
                .formatted(parentQuestion.id, referredQuestionId)
            );
        
        if (referredQuestion instanceof QuestionGroup)
    		throw new WrongDefinitionStructureException(
                "A condition of question (id:\"%s\") is referring directly to a question group (id:\"%s\")"
                .formatted(parentQuestion.id, referredQuestionId)
            );

        if (!referredQuestion.index.isBefore(parentQuestion.index))
    		throw new WrongDefinitionStructureException(
                "A condition can only refer to a question (id:\"%s\") that comes before the current question (id:\"%s\")"
                .formatted(referredQuestionId, parentQuestion.id)
            );
        
        if (!referredQuestion.meetsToValue(definition.value()))
    		throw new WrongDefinitionStructureException(
                "A condition value in question (id:\"%s\") doesn't meet values of referred question (id:\"%s\")"
                .formatted(parentQuestion.id, referredQuestionId)
            );
    }

    public boolean isFulfilled(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        return referredQuestion!=null
            && referredQuestion.isActive(questionaryAnswers)
            && questionaryAnswers
                .answers()
                .getOrDefault(definition.questionId(), Set.of())
                .contains(definition.value());
    }
}
