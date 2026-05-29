package net.schwarzbaer.spring.questionary.models.definitions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ConditionValueDef.BoolValueDef  .class, name = "BOOL"  ),
    @JsonSubTypes.Type(value = ConditionValueDef.StringValueDef.class, name = "STRING")
})
public abstract class ConditionValueDef
{
    @Setter @Getter @ToString
    public static class BoolValueDef extends ConditionValueDef
    {
        private boolean value;
    }

    @Setter @Getter @ToString
    public static class StringValueDef extends ConditionValueDef
    {
        private String value;
    }
}
