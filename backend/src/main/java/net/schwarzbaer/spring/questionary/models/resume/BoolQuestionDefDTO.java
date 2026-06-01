package net.schwarzbaer.spring.questionary.models.resume;

import lombok.NonNull;
import lombok.ToString;

@ToString(callSuper=true)
public class BoolQuestionDefDTO extends QuestionDefDTO
{
    public BoolQuestionDefDTO(@NonNull String id, @NonNull String text)
    {
        super(id, text);
    }
}
