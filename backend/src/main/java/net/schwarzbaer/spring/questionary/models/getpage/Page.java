package net.schwarzbaer.spring.questionary.models.getpage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.NonNull;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BoolPage           .class, name = "BOOL"    ),
    @JsonSubTypes.Type(value = ChoicePage.Single  .class, name = "SINGLE"  ),
    @JsonSubTypes.Type(value = ChoicePage.Multiple.class, name = "MULTIPLE") 
})
public class Page
{
    @NonNull private final String id;
    @NonNull private final String text;
}
