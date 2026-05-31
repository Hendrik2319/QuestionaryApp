package net.schwarzbaer.spring.questionary.models.answers;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.NonNull;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = QuestionAnswerValue.BoolValue  .class, name = "BOOL"  ),
    @JsonSubTypes.Type(value = QuestionAnswerValue.StringValue.class, name = "STRING")
})
public interface QuestionAnswerValue
{
    public record BoolValue(boolean value) implements QuestionAnswerValue
    {}

    public record StringValue(@NonNull String value) implements QuestionAnswerValue
    {}
}
