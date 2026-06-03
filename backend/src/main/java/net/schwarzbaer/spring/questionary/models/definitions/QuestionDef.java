package net.schwarzbaer.spring.questionary.models.definitions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BoolQuestionDef           .class, name = "BOOL"    ),
    @JsonSubTypes.Type(value = ChoiceQuestionDef.Single  .class, name = "SINGLE"  ),
    @JsonSubTypes.Type(value = ChoiceQuestionDef.Multiple.class, name = "MULTIPLE"),
    @JsonSubTypes.Type(value = QuestionGroupDef          .class, name = "GROUP"   ) 
})
@Setter @Getter @ToString
public abstract class QuestionDef
{
    private String id;
    private String text;
    private ConditionsGroupDef conditions;
    private final SelectionType selectionType;

    protected QuestionDef(SelectionType selectionType)
    {
        this.selectionType = selectionType;
    }

    protected void setAllValuesExceptConditions(@NonNull QuestionDef other)
    {
        this.id   = other.id;
        this.text = other.text;
    }
}
