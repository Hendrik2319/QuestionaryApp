package net.schwarzbaer.spring.questionary.models.questionary;

import lombok.NonNull;

public record QuestionPageInfo(@NonNull Question<?> question, int pageIndex, boolean isFirst)
{
    QuestionPageInfo(@NonNull Question<?> question, int pageIndex)
    {
        this(question, pageIndex, pageIndex==0);
    }
}