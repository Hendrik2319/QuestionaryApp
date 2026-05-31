package net.schwarzbaer.spring.questionary.models.answers;

import lombok.NonNull;

public record SetAnswerDTO (
    @NonNull String sessionId,
    @NonNull String questionId,
    @NonNull ChangeType changeType,
    @NonNull QuestionAnswerValue answerValue
)
{
    public enum ChangeType { SET, UNSET }
}
