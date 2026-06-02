package net.schwarzbaer.spring.questionary.models.page;

import java.util.List;

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
    @JsonSubTypes.Type(value = BoolPage           .class, name = "BOOL"    ),
    @JsonSubTypes.Type(value = ChoicePage.Single  .class, name = "SINGLE"  ),
    @JsonSubTypes.Type(value = ChoicePage.Multiple.class, name = "MULTIPLE") 
})
@RequiredArgsConstructor @Getter @ToString
public abstract class Page
{
    @NonNull
    private final String id;
    @NonNull
    private final List<String> texts;
    private final boolean firstPage;
}
