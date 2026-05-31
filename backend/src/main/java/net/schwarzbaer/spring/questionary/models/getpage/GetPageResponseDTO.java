package net.schwarzbaer.spring.questionary.models.getpage;

public record GetPageResponseDTO(
    
)
{
    public record Page (
        String id,
        String text

    )
    {}

}
