package net.schwarzbaer.spring.questionary.models.getpage;

import java.util.List;

import net.schwarzbaer.spring.questionary.models.definitions.OptionDef;

public class ChoicePage extends Page
{
    private List<OptionDef> options;

    public class Single extends ChoicePage
    {
    }

    public class Multiple extends ChoicePage
    {
    }

}
