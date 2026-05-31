package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionDef;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionsGroupDef;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionsGroupDef.AggregationType;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;

public class ConditionsGroup
{
    private final @NonNull ConditionsGroupDef conditionsGroupDef;
    private final @NonNull AggregationType type;
    private final @NonNull List<Condition> conditions;

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

    void dereferenceIds(Function<String,Question<?>> findQuestion)
    {
        for (Condition condition : conditions)
            condition.dereferenceIds(findQuestion);
    }

    void checkDefinitionStructure(@NonNull Question<?> parentQuestion) throws WrongDefinitionStructureException
    {
        for (Condition condition : conditions)
            condition.checkDefinitionStructure(parentQuestion);
    }
}
