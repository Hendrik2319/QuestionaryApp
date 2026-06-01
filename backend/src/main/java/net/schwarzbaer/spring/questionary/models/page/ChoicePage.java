package net.schwarzbaer.spring.questionary.models.page;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import net.schwarzbaer.spring.questionary.models.definitions.OptionDef;

@Getter @ToString(callSuper=true)
public abstract class ChoicePage extends Page
{
    @NonNull private final List<OptionDef> options;

    protected ChoicePage(@NonNull String id, @NonNull List<String> texts, boolean isFirst, @NonNull List<OptionDef> options)
    {
        super(id, texts, isFirst);
        this.options = options;
    }

    @ToString(callSuper=true)
    public static class Single extends ChoicePage
    {
        public Single(@NonNull String id, @NonNull List<String> texts, boolean isFirst, @NonNull List<OptionDef> options)
        {
            super(id, texts, isFirst, options);
        }
    }

    @ToString(callSuper=true)
    public static class Multiple extends ChoicePage
    {
        public Multiple(@NonNull String id, @NonNull List<String> texts, boolean isFirst, @NonNull List<OptionDef> options)
        {
            super(id, texts, isFirst, options);
        }
    }
}
