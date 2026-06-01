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
    @JsonSubTypes.Type(value = BoolQuestionResumeDTO              .class, name = "BOOL"    ),
    @JsonSubTypes.Type(value = ChoiceQuestionResumeDTO.SingleDTO  .class, name = "SINGLE"  ),
    @JsonSubTypes.Type(value = ChoiceQuestionResumeDTO.MultipleDTO.class, name = "MULTIPLE"),
    @JsonSubTypes.Type(value = QuestionGroupResumeDTO             .class, name = "GROUP"   ) 
})
@RequiredArgsConstructor @Getter @ToString
public abstract class QuestionResumeDTO
{
    @NonNull private final String id;
    @NonNull private final String text;
}
