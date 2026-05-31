package net.schwarzbaer.spring.questionary.models.definitions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.NonNull;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ConditionValueDef.BoolValueDef  .class, name = "BOOL"  ),
    @JsonSubTypes.Type(value = ConditionValueDef.StringValueDef.class, name = "STRING")
})
public interface ConditionValueDef
{
    public record BoolValueDef(boolean value) implements ConditionValueDef
    {}

    public record StringValueDef(@NonNull String value) implements ConditionValueDef
    {}
}
