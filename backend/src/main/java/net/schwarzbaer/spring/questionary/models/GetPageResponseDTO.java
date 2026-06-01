package net.schwarzbaer.spring.questionary.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.page.Page;
import net.schwarzbaer.spring.questionary.models.resume.Resume;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = GetPageResponseDTO.PageDTO  .class, name = "PAGE"  ),
    @JsonSubTypes.Type(value = GetPageResponseDTO.ResumeDTO.class, name = "RESUME") 
})
public interface GetPageResponseDTO
{
    public record PageDTO(@NonNull Page page, @NonNull Set<PolymorphicValue> pageData) implements GetPageResponseDTO
    {}

    public record ResumeDTO(@NonNull Resume resume) implements GetPageResponseDTO
    {}
}
