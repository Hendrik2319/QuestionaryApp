package net.schwarzbaer.spring.questionary.models;

import lombok.NonNull;

public record GetPageRequestDTO(
    @NonNull String sessionId,
    String questionId, // no questionId means: get first page/question
    @NonNull Direction direction
)
{
    public enum Direction { PREV, NEXT }
}
