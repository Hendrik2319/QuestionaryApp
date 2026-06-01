package net.schwarzbaer.spring.questionary.models.resume;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter @ToString(callSuper=true)
public class BoolQuestionResumeDTO extends QuestionResumeDTO
{
    private final Boolean answer;

    public BoolQuestionResumeDTO(@NonNull String id, @NonNull String text, Boolean answer)
    {
        super(id, text);
        this.answer = answer;
    }
}
