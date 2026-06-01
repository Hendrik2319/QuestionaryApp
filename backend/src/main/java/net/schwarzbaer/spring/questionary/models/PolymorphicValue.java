package net.schwarzbaer.spring.questionary.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.NonNull;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PolymorphicValue.BoolValue  .class, name = "BOOL"  ),
    @JsonSubTypes.Type(value = PolymorphicValue.StringValue.class, name = "STRING")
})
public interface PolymorphicValue
{
    public record BoolValue(boolean value) implements PolymorphicValue
    {}

    public record StringValue(@NonNull String value) implements PolymorphicValue
    {}
}
