package net.schwarzbaer.spring.questionary.models;

import lombok.NonNull;

public record InitialValuesDTO (
    @NonNull String sessionId,
    boolean needQuestionary
)
{}
