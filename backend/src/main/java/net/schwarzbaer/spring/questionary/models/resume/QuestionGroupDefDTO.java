package net.schwarzbaer.spring.questionary.models.resume;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter @ToString(callSuper=true)
public class QuestionGroupDefDTO extends QuestionDefDTO
{
    @NonNull private final List<QuestionDefDTO> subQuestions;

    public QuestionGroupDefDTO(@NonNull String id, @NonNull String text, @NonNull List<QuestionDefDTO> subQuestions)
    {
        super(id, text);
        this.subQuestions = subQuestions;
    }
}
