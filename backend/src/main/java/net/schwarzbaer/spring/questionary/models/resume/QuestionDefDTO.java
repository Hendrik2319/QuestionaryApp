package net.schwarzbaer.spring.questionary.models.resume;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BoolQuestionDefDTO              .class, name = "BOOL"    ),
    @JsonSubTypes.Type(value = ChoiceQuestionDefDTO.SingleDTO  .class, name = "SINGLE"  ),
    @JsonSubTypes.Type(value = ChoiceQuestionDefDTO.MultipleDTO.class, name = "MULTIPLE"),
    @JsonSubTypes.Type(value = QuestionGroupDefDTO             .class, name = "GROUP"   ) 
})
@RequiredArgsConstructor @Getter @ToString
public class QuestionDefDTO
{
    @NonNull private final String id;
    @NonNull private final String text;
}
