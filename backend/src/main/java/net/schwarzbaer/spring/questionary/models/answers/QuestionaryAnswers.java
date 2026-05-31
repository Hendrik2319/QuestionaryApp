package net.schwarzbaer.spring.questionary.models.answers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.answers.QuestionAnswerDTO.ChangeType;
import net.schwarzbaer.spring.questionary.models.definitions.SelectionType;
import net.schwarzbaer.spring.questionary.models.questionary.Question;
import net.schwarzbaer.spring.questionary.models.questionary.QuestionGroup;
import net.schwarzbaer.spring.questionary.models.questionary.Questionary;

public class QuestionaryAnswers
{
    public  final @NonNull String sessionId;
    public  final @NonNull LocalDateTime createTime;
    private final @NonNull Questionary questionary;
    private final @NonNull Map<String,Set<QuestionAnswerValue>> answers;

    public QuestionaryAnswers(@NonNull String sessionId, @NonNull LocalDateTime createTime, @NonNull Questionary questionary)
    {
        this.sessionId = sessionId;
        this.createTime = createTime;
        this.questionary = questionary;
        answers = new HashMap<>();
    }

    public void setAnswer(@NonNull String questionId, @NonNull ChangeType changeType, @NonNull QuestionAnswerValue answerValue) throws NoSuchElementException, IllegalArgumentException
    {
        Question<?> question = questionary.getQuestion(questionId);
        if (question==null)
            throw new NoSuchElementException("Unknown question ID: %s".formatted(questionId));

        if (question instanceof QuestionGroup)
            throw new IllegalArgumentException("Given answer targets directly a question group (id:\"%s\")".formatted(question.id));

        if (question.meetToAnswerValue(answerValue))
            throw new IllegalArgumentException("Given answer value (%s) does'nt fit to question (id:\"%s\")".formatted(answerValue, question.id));

        Set<QuestionAnswerValue> answerSet = answers.computeIfAbsent(questionId, id->new HashSet<>());
        switch (changeType)
        {
        case UNSET:
            answerSet.remove(answerValue);
            break;

        case SET:
            SelectionType selectionType = question.getSelectionType();
            if (selectionType!=null) // only QuestionGroup is allowed to have a selectionType of NULL
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
}
