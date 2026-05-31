package net.schwarzbaer.spring.questionary.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.schwarzbaer.spring.questionary.models.InitialValues;
import net.schwarzbaer.spring.questionary.models.answers.QuestionAnswerDTO;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;
import net.schwarzbaer.spring.questionary.services.MainService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController
{
    private final MainService mainService;

    @PostMapping("/setquestionary")
    public void receiveQuestionary(@RequestBody String data) throws WrongDefinitionStructureException
    {
        mainService.setQuestionary(data);
    }

    @PostMapping("/setanswer")
    public void receiveAnswer(@RequestBody QuestionAnswerDTO questionAnswerDTO)
    {
        mainService.setAnswer(questionAnswerDTO);
    }

    @GetMapping("/getinit")
    public InitialValues sendInitialValues()
    {
        return mainService.generateInitialValues();
    }
}
