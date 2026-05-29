package net.schwarzbaer.spring.questionary.models.definitions;

import java.util.List;

public record QuestionaryDef (
    String title,
    List<QuestionDef> questions
) {}
