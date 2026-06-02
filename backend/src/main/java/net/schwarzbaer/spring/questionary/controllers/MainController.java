package net.schwarzbaer.spring.questionary.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.schwarzbaer.spring.questionary.models.GetPageRequestDTO;
import net.schwarzbaer.spring.questionary.models.GetPageResponseDTO;
import net.schwarzbaer.spring.questionary.models.InitialValuesDTO;
import net.schwarzbaer.spring.questionary.models.PageDirection;
import net.schwarzbaer.spring.questionary.models.QuestionaryTitle;
import net.schwarzbaer.spring.questionary.models.answers.SetAnswerDTO;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;
import net.schwarzbaer.spring.questionary.services.MainService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController
{
    private final MainService mainService;

    @PostMapping("/questionary")
    public QuestionaryTitle receiveQuestionary(@RequestBody String data) throws WrongDefinitionStructureException
    {
        return mainService.setQuestionary(data);
    }

    @PostMapping("/{sessionId}/page/{direction}")
    public GetPageResponseDTO sendPage(@NonNull @PathVariable String sessionId, @NonNull @PathVariable PageDirection direction, @RequestBody GetPageRequestDTO requestDTO)
    {
        return mainService.getPage(sessionId, direction, requestDTO);
    }

    @PostMapping("/{sessionId}/answer")
    public void receiveAnswer(@NonNull @PathVariable String sessionId, @RequestBody SetAnswerDTO requestDTO)
    {
        mainService.setAnswer(sessionId, requestDTO);
    }

    @GetMapping("/init")
    public InitialValuesDTO sendInitialValues()
    {
        return mainService.generateInitialValues();
    }
}
