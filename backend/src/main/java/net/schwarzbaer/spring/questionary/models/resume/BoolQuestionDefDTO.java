package net.schwarzbaer.spring.questionary.models.resume;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter @ToString(callSuper=true)
public class BoolQuestionDefDTO extends QuestionDefDTO
{
    private final Boolean answer;

    public BoolQuestionDefDTO(@NonNull String id, @NonNull String text, Boolean answer)
    {
        super(id, text);
        this.answer = answer;
    }
}
