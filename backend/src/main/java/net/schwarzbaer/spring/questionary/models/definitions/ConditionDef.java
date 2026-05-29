package net.schwarzbaer.spring.questionary.models.definitions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class ConditionDef
{
    private String questionId;
    private ConditionValueDef value;
}
