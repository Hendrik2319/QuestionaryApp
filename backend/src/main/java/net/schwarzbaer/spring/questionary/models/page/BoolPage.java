package net.schwarzbaer.spring.questionary.models.page;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter @ToString(callSuper=true)
public class BoolPage extends Page
{
    public BoolPage(@NonNull String id, @NonNull List<String> texts, boolean firstPage)
    {
        super(id, texts, firstPage);
    }
}
