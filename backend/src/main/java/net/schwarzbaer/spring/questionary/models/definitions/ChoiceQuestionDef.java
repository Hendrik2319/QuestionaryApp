package net.schwarzbaer.spring.questionary.models.definitions;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString(callSuper=true)
public abstract class ChoiceQuestionDef extends QuestionDef
{
    private List<OptionDef> options;

    protected ChoiceQuestionDef(@NonNull SelectionType selectionType)
    {
        super(selectionType);
    }

    @Setter @Getter @ToString(callSuper=true)
    public static class Multiple extends ChoiceQuestionDef
    {
        public Multiple()
        {
            super(SelectionType.Multiple);
        }
    }
    
    @Setter @Getter @ToString(callSuper=true)
    public static class Single extends ChoiceQuestionDef
    {
        public Single()
        {
            super(SelectionType.Single);
        }
    }
}
