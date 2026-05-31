package net.schwarzbaer.spring.questionary.models.definitions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString(callSuper=true)
public class BoolQuestionDef extends QuestionDef
{
    public BoolQuestionDef()
    {
        super(SelectionType.Single);
    }
}
