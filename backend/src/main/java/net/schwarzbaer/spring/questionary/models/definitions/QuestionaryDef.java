package net.schwarzbaer.spring.questionary.models.definitions;

import java.util.List;

import lombok.NonNull;

public record QuestionaryDef (
    @NonNull String title,
    @NonNull List<QuestionDef> questions
)
{}
