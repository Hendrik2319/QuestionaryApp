package net.schwarzbaer.spring.questionary.models.definitions;

import lombok.NonNull;

public record OptionDef (
    @NonNull String value,
    String label
)
{
    public OptionDef createCopy()
    {
        return new OptionDef(value, label);
    }
}
