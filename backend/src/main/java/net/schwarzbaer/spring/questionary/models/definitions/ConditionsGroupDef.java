package net.schwarzbaer.spring.questionary.models.definitions;

import java.util.List;

import lombok.NonNull;

public record ConditionsGroupDef (
    @NonNull AggregationType type,
    @NonNull List<ConditionDef> conditions
)
{
    public enum AggregationType { ALL, ONE }
}
