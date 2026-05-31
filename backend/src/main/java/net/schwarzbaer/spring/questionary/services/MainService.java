package net.schwarzbaer.spring.questionary.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.schwarzbaer.spring.questionary.models.InitialValues;
import net.schwarzbaer.spring.questionary.models.answers.QuestionAnswerDTO;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionaryDef;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;
import net.schwarzbaer.spring.questionary.models.questionary.Questionary;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MainService
{
    private final ObjectMapper objectMapper;
    private       Questionary currentQuestionary = null;
    private final SessionIdGenerator sessionIdGenerator = new SessionIdGenerator(8);
    private final Map<String,QuestionaryAnswers> allQuestionaryAnswers = new HashMap<>();

    public void setQuestionary(String data) throws WrongDefinitionStructureException
    {
        QuestionaryDef definition = objectMapper.readValue(data, QuestionaryDef.class);
        currentQuestionary = new Questionary(definition);
        currentQuestionary.checkDefinitionStructure();
        
        System.out.printf("[POST] /api/setquestionary : receiveQuestionary()%n   data = \"%s\"%n   -> value = %s%n", data, definition);
        // TODO Auto-generated method stub
    }

    public void setAnswer(@NonNull QuestionAnswerDTO questionAnswerDTO) throws NoSuchElementException
    {
        QuestionaryAnswers questionaryAnswers = allQuestionaryAnswers.get(questionAnswerDTO.sessionId());
        if (questionaryAnswers==null)
            throw new NoSuchElementException("Unknown session ID: %s".formatted(questionAnswerDTO.sessionId()));

        questionaryAnswers.setAnswer(
            questionAnswerDTO.questionId(),
            questionAnswerDTO.changeType(),
            questionAnswerDTO.answerValue()
        );
    }

    public InitialValues generateInitialValues()
    {
        String sessionId = getNewSessionId();

        LocalDateTime now = LocalDateTime.now();
        removeOldSessions(now, Duration.ofHours(5));
        allQuestionaryAnswers.put(sessionId, new QuestionaryAnswers(sessionId, now, currentQuestionary));

        return new InitialValues(sessionId, currentQuestionary == null);
    }

    private String getNewSessionId()
    {
        String sessionId;
        while (allQuestionaryAnswers.containsKey(sessionId = sessionIdGenerator.generate()));
        return sessionId;
    }

    private void removeOldSessions(LocalDateTime now, Duration maxActiveDuration)
    {
        List<String> sessionIds = new ArrayList<>(allQuestionaryAnswers.keySet());
        for (String otherSessionId : sessionIds)
        {
            QuestionaryAnswers otherAnswers = allQuestionaryAnswers.get(otherSessionId);
            Duration durationActive = Duration.between(otherAnswers.createTime, now);
            if (durationActive.compareTo(maxActiveDuration) > 0)
                allQuestionaryAnswers.remove(otherSessionId);
        }
    }
}
