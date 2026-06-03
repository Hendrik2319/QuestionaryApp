package net.schwarzbaer.spring.questionary.models.resume;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter @ToString(callSuper=true)
public class BoolQuestionResumeDTO extends QuestionResumeDTO
{
    private final Boolean answer;

    public BoolQuestionResumeDTO(@NonNull String id, @NonNull String text, boolean active, Boolean answer)
    {
        super(id, text, active);
        this.answer = answer;
    }
}
