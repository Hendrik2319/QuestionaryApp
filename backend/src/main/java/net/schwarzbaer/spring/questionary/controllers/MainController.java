package net.schwarzbaer.spring.questionary.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.schwarzbaer.spring.questionary.models.definitions.QuestionaryDef;
import net.schwarzbaer.spring.questionary.models.errors.ErrorMessage;
import tools.jackson.core.JacksonException;
import tools.jackson.core.exc.JacksonIOException;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.DatabindException;
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
        QuestionaryDef value = objectMapper.readValue(data, QuestionaryDef.class);
        System.out.printf("[POST] /api/setquestionary : receiveQuestionary()%n   data = \"%s\"%n   -> value = %s%n", data, value);
    }

    @ExceptionHandler({JacksonIOException.class, StreamReadException.class, DatabindException.class, JacksonException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage handleIllegalArgumentException(Exception ex) {
        String msg = "%s: %s".formatted(ex.getClass().getSimpleName(), ex.getMessage());
        System.err.println(msg);
        return new ErrorMessage(msg);
    }
}
