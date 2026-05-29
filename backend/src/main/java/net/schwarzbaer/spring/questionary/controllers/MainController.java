package net.schwarzbaer.spring.questionary.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.schwarzbaer.spring.questionary.models.definitions.QuestionaryDef;
import tools.jackson.databind.ObjectMapper;

@RestController 
@RequestMapping("/api")
public class MainController
{
    private final ObjectMapper objectMapper;

    public MainController(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @PostMapping("/setquestionary") 
    public void receiveQuestionary(@RequestBody String data)
    {
        //System.out.printf("[POST] /api/setquestionary : receiveQuestionary( data:\"%s\" )%n", data);
        QuestionaryDef value = objectMapper.readValue(data, QuestionaryDef.class);
        System.out.printf("[POST] /api/setquestionary : receiveQuestionary()%n   data = \"%s\"%n   -> value = %s%n", data, value);
    }
}
