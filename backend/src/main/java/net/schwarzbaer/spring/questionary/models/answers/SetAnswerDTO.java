package net.schwarzbaer.spring.questionary.models.answers;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.PolymorphicValue;

public record SetAnswerDTO (
    @NonNull String questionId,
    @NonNull ChangeType changeType,
    @NonNull PolymorphicValue answerValue
)
{
    public enum ChangeType { SET, UNSET }
}
