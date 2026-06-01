package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionDef;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionsGroupDef;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionsGroupDef.AggregationType;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;

public class ConditionsGroup
{
    @NonNull private final ConditionsGroupDef conditionsGroupDef;
    @NonNull private final AggregationType type;
    @NonNull private final List<Condition> conditions;

    ConditionsGroup(@NonNull ConditionsGroupDef conditionsGroupDef)
    {
        this.conditionsGroupDef = conditionsGroupDef;
        type = conditionsGroupDef.type();
        conditions = new ArrayList<>();

        List<ConditionDef> conditionDefs = conditionsGroupDef.conditions();
        if (conditionDefs!=null && !conditionDefs.isEmpty())
        {
            for (ConditionDef def : conditionDefs)
                conditions.add(new Condition(def));
        }
    }

    void checkDefinitionStructure(@NonNull Question<?> parentQuestion, @NonNull Function<String, Question<?>> findQuestion) throws WrongDefinitionStructureException
    {
        for (Condition condition : conditions)
            condition.checkDefinitionStructure(parentQuestion, findQuestion);
    }

    boolean isEmpty()
    {
        return conditions.isEmpty();
    }

    public boolean isFulfilled(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        if (conditions.isEmpty())
            return true;

        for (Condition condition : conditions)
        {
            if (condition.isFulfilled(questionaryAnswers))
            {
                if (type==AggregationType.ONE) return true;
            }
            else
            {
                if (type==AggregationType.ALL) return false;
            }
        }
        switch (type)
        {
            case ALL: return true;
            case ONE: return false;
            default: throw new IllegalStateException("ConditionsGroupDef.AggregationType has an unexpected enum value: %s".formatted(type));
        }
    }
}
