package net.schwarzbaer.spring.questionary.models.resume;

import java.util.List;
import java.util.Set;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.answers.QuestionAnswerValue;

public record Resume(
    @NonNull List<QuestionDefDTO> questions,
    @NonNull List<QuestionAnswers> answers
)
{
    public record QuestionAnswers(
        @NonNull String questionId,
        @NonNull Set<QuestionAnswerValue> answers
    )
    {}
}
