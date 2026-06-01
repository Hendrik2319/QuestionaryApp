package net.schwarzbaer.spring.questionary.models.definitions;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.PolymorphicValue;

public record ConditionDef (
    @NonNull String questionId,
    @NonNull PolymorphicValue value
)
{}
