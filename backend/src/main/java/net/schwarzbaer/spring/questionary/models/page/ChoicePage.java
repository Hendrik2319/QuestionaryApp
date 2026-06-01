package net.schwarzbaer.spring.questionary.models.page;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import net.schwarzbaer.spring.questionary.models.definitions.OptionDef;

@Getter @ToString(callSuper=true)
public class ChoicePage extends Page
{
    @NonNull private final List<OptionDef> options;

    protected ChoicePage(@NonNull String id, @NonNull String text, boolean isFirst, @NonNull List<OptionDef> options)
    {
        super(id, text, isFirst);
        this.options = options;
    }

    @ToString(callSuper=true)
    public class Single extends ChoicePage
    {
        public Single(@NonNull String id, @NonNull String text, boolean isFirst, @NonNull List<OptionDef> options)
        {
            super(id, text, isFirst, options);
        }
    }

    @ToString(callSuper=true)
    public class Multiple extends ChoicePage
    {
        public Multiple(@NonNull String id, @NonNull String text, boolean isFirst, @NonNull List<OptionDef> options)
        {
            super(id, text, isFirst, options);
        }
    }
}
