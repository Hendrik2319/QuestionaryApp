package net.schwarzbaer.spring.questionary.models.definitions;

import lombok.NonNull;

public record ConditionDef (
    @NonNull String questionId,
    @NonNull ConditionValueDef value
)
{}
