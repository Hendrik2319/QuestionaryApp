package net.schwarzbaer.spring.questionary.models.page;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter @ToString(callSuper=true)
public class BoolPage extends Page
{
    public BoolPage(@NonNull String id, @NonNull String text, boolean isFirst)
    {
        super(id, text, isFirst);
    }
}
