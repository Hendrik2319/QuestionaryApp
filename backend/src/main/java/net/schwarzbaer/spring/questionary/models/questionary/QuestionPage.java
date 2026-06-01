package net.schwarzbaer.spring.questionary.models.questionary;

import lombok.NonNull;

public record QuestionPage(@NonNull Question<?> question, int pageIndex, boolean isFirst)
{
    QuestionPage(@NonNull Question<?> question, int pageIndex)
    {
        this(question, pageIndex, pageIndex==0);
    }
}