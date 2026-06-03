package net.schwarzbaer.spring.questionary.models.resume;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter @ToString(callSuper=true)
public class QuestionGroupResumeDTO extends QuestionResumeDTO
{
    @NonNull private final List<QuestionResumeDTO> subQuestions;

    public QuestionGroupResumeDTO(@NonNull String id, @NonNull String text, boolean active, @NonNull List<QuestionResumeDTO> subQuestions)
    {
        super(id, text, active);
        this.subQuestions = subQuestions;
    }
}
