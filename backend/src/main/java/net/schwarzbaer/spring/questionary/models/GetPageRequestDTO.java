package net.schwarzbaer.spring.questionary.models;

public record GetPageRequestDTO(
    String questionId // no questionId means: get first/last page/question
)
{
}
