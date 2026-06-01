package net.schwarzbaer.spring.questionary.models.answers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.PolymorphicValue;

public record QuestionaryAnswers (
    @NonNull String sessionId,
    @NonNull LocalDateTime createTime,
    @NonNull Map<String,Set<PolymorphicValue>> answers
)
{
    public QuestionaryAnswers(@NonNull String sessionId, @NonNull LocalDateTime createTime)
    {
        this(sessionId, createTime, new HashMap<>());
    }
}
