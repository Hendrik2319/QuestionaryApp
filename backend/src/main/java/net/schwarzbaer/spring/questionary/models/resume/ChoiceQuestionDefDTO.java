package net.schwarzbaer.spring.questionary.models.resume;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import net.schwarzbaer.spring.questionary.models.definitions.OptionDef;

@Getter @ToString(callSuper=true)
public abstract class ChoiceQuestionDefDTO extends QuestionDefDTO
{
    @NonNull private final List<OptionDef> answers;

    protected ChoiceQuestionDefDTO(@NonNull String id, @NonNull String text, @NonNull List<OptionDef> answers)
    {
        super(id, text);
        this.answers = answers;
    }

    @Getter @ToString(callSuper=true)
    public static class MultipleDTO extends ChoiceQuestionDefDTO
    {
        public MultipleDTO(@NonNull String id, @NonNull String text, @NonNull List<OptionDef> answers)
        {
            super(id, text, answers);
        }
    }

    @Getter @ToString(callSuper=true)
    public static class SingleDTO extends ChoiceQuestionDefDTO
    {
        public SingleDTO(@NonNull String id, @NonNull String text, @NonNull List<OptionDef> answers)
        {
            super(id, text, answers);
        }
    }
}
