package net.schwarzbaer.spring.questionary.models;

import lombok.NonNull;

public record InitialValues (
    @NonNull String sessionId,
    boolean needQuestionary
)
{}
