package net.schwarzbaer.spring.questionary.services;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionaryDef;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;
import net.schwarzbaer.spring.questionary.models.questionary.Questionary;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MainService
{
    private final ObjectMapper objectMapper;
    private Questionary currentQuestionary = null;

    public void setQuestionary(String data) throws WrongDefinitionStructureException
    {
        QuestionaryDef definition = objectMapper.readValue(data, QuestionaryDef.class);
        currentQuestionary = new Questionary(definition);
        currentQuestionary.checkDefinitionStructure();
        
        System.out.printf("[POST] /api/setquestionary : receiveQuestionary()%n   data = \"%s\"%n   -> value = %s%n", data, definition);
        // TODO Auto-generated method stub
    }
}
