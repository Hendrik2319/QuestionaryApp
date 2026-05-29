package net.schwarzbaer.spring.questionary.models.definitions;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class ConditionsGroupDef
{
    public enum AggregationType { ALL, ONE }

    private AggregationType type;
    private List<ConditionDef> conditions;
}
