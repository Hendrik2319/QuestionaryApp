package net.schwarzbaer.spring.questionary.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.schwarzbaer.spring.questionary.models.InitialValuesDTO;
import net.schwarzbaer.spring.questionary.models.answers.QuestionAnswerValue;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.answers.SetAnswerDTO;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionaryDef;
import net.schwarzbaer.spring.questionary.models.definitions.SelectionType;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;
import net.schwarzbaer.spring.questionary.models.getpage.GetPageRequestDTO;
import net.schwarzbaer.spring.questionary.models.getpage.GetPageRequestDTO.Direction;
import net.schwarzbaer.spring.questionary.models.getpage.GetPageResponseDTO;
import net.schwarzbaer.spring.questionary.models.questionary.Question;
import net.schwarzbaer.spring.questionary.models.questionary.QuestionGroup;
import net.schwarzbaer.spring.questionary.models.questionary.Questionary;
import net.schwarzbaer.spring.questionary.models.questionary.Questionary.QuestionPage;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MainService
{
    private final ObjectMapper objectMapper;
    private       Questionary currentQuestionary = null;
    private final SessionIdGenerator sessionIdGenerator = new SessionIdGenerator(8);
    private final Map<String,QuestionaryAnswers> allQuestionaryAnswers = new HashMap<>();

    public void setQuestionary(@NonNull String data) throws WrongDefinitionStructureException
    {
        QuestionaryDef definition = objectMapper.readValue(data, QuestionaryDef.class);
        currentQuestionary = new Questionary(definition);
        currentQuestionary.checkDefinitionStructure();
        //System.out.printf("[POST] /api/setquestionary : receiveQuestionary()%n   data = \"%s\"%n   -> value = %s%n", data, definition);
    }

    public GetPageResponseDTO getPage(@NonNull GetPageRequestDTO getPageRequestDTO) throws NoSuchElementException
    {
        if (currentQuestionary==null)
            throw new NoSuchElementException("No questionary uploaded");

        String questionId = getPageRequestDTO.questionId();
        @NonNull
        Direction direction = getPageRequestDTO.direction();
        @NonNull
        QuestionaryAnswers questionaryAnswers = getQuestionaryAnswers(getPageRequestDTO.sessionId());

        QuestionPage currentPage, nextPage;
        if (questionId==null)
        {
            currentPage = null;
            nextPage = currentQuestionary.getFirstPage();
            if (nextPage==null)
                throw new NoSuchElementException("There is no first page in this questionary");
        }
        else
        {
            currentPage = currentQuestionary.getPage(questionId);
            if (currentPage==null)
                throw new NoSuchElementException("Unknown ID of current question: %s".formatted(questionId));
            
            nextPage = currentQuestionary.getNextPage(
                currentPage.pageIndex(),
                direction,
                questionaryAnswers
            );
        }

        if (nextPage==null)
        {
            switch (direction)
            {
            case PREV: // currentPage is first page
                nextPage = currentPage;

            case NEXT:
                // page: null
                // resume: generate()
                return /* DTO */ null;
            }
        }

        // page: nextPage
        // resume: null
        return /* DTO */ null;
    }

    public void setAnswer(@NonNull SetAnswerDTO setAnswerDTO) throws NoSuchElementException, IllegalArgumentException
    {
        if (currentQuestionary==null)
            throw new NoSuchElementException("No questionary uploaded");

        @NonNull
        QuestionaryAnswers questionaryAnswers = getQuestionaryAnswers(setAnswerDTO.sessionId());
        @NonNull
        String questionId = setAnswerDTO.questionId();
        @NonNull
        QuestionAnswerValue answerValue = setAnswerDTO.answerValue();

        Question<?> question = currentQuestionary.getQuestion(questionId);
        if (question==null)
            throw new NoSuchElementException("Unknown question ID: %s".formatted(questionId));

        if (question instanceof QuestionGroup)
            throw new IllegalArgumentException("Given answer targets directly a question group (id:\"%s\")".formatted(question.id));

        if (question.meetToAnswerValue(answerValue))
            throw new IllegalArgumentException("Given answer value (%s) does'nt fit to question (id:\"%s\")".formatted(answerValue, question.id));

        Set<QuestionAnswerValue> answerSet = questionaryAnswers.answers().computeIfAbsent(questionId, id->new HashSet<>());
        switch (setAnswerDTO.changeType())
        {
        case UNSET:
            answerSet.remove(answerValue);
            break;

        case SET:
            SelectionType selectionType = question.getSelectionType();
            if (selectionType!=null) // only a QuestionGroup is allowed to have a selectionType of NULL
                switch (selectionType)
                {
                case Multiple:
                    answerSet.add(answerValue);
                    break;

                case Single:
                    answerSet.clear();
                    answerSet.add(answerValue);
                    break;
                }
            break;
        }
    }

    private @NonNull QuestionaryAnswers getQuestionaryAnswers(@NonNull String sessionId) throws NoSuchElementException
    {
        QuestionaryAnswers questionaryAnswers = allQuestionaryAnswers.get(sessionId);
        if (questionaryAnswers==null)
            throw new NoSuchElementException("Unknown session ID: %s".formatted(sessionId));
        return questionaryAnswers;
    }

    public InitialValuesDTO generateInitialValues()
    {
        String sessionId = getNewSessionId();

        LocalDateTime now = LocalDateTime.now();
        removeOldSessions(now, Duration.ofHours(5));
        allQuestionaryAnswers.put(sessionId, new QuestionaryAnswers(sessionId, now));

        return new InitialValuesDTO(sessionId, currentQuestionary == null);
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
            Duration durationActive = Duration.between(otherAnswers.createTime(), now);
            if (durationActive.compareTo(maxActiveDuration) > 0)
                allQuestionaryAnswers.remove(otherSessionId);
        }
    }
}
