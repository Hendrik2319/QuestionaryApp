package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionaryDef;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;
import net.schwarzbaer.spring.questionary.models.getpage.GetPageRequestDTO.Direction;

public class Questionary
{
    private final QuestionaryDef definition;
    private final @NonNull String title;
    private final Map<String,Question<?>> questions;
    private final List<Question<?>> questionPages;

    public Questionary(@NonNull QuestionaryDef definition) throws WrongDefinitionStructureException
    {
        this.definition = definition;
        title = this.definition.title();
        questions = new HashMap<>();

        List<QuestionDef> questionDefs = this.definition.questions();
        if (questionDefs != null && !questionDefs.isEmpty())
        {
            for (int i=0; i<questionDefs.size(); i++)
            {
                Question<?> question = Question.createQuestion(null, QuestionIndex.createIndex(i), questionDefs.get(i));
                addToMap(questions, question);
                
                if (question instanceof QuestionGroup group)
                    group.forEachSubQuestion((QuestionGroup.ForEachSubQuestionAction) subQuestion -> addToMap(questions, subQuestion));
            }

            questionPages = questions
                .values()
                .stream()
                .filter(q -> !(q instanceof QuestionGroup))
                .sorted(Comparator.comparing(q->q.index))
                .toList();

            for (Question<?> question : questions.values())
                question.dereferenceIdsInConditions(questions::get);
        }
        else
            questionPages = new ArrayList<>();

    }

    private static void addToMap(@NonNull Map<String,Question<?>> questionsMap, @NonNull Question<?> question) throws WrongDefinitionStructureException
    {
        if (question.id==null)
            throw new WrongDefinitionStructureException("Question ID must not be NULL.");

        if (question.id.isBlank())
            throw new WrongDefinitionStructureException("Question ID must not be empty or blank.");

        if (questionsMap.containsKey(question.id))
            throw new WrongDefinitionStructureException("Question ID \"%s\" is not unique.".formatted(question.id));

        questionsMap.put(question.id, question);
    }

    public void checkDefinitionStructure() throws WrongDefinitionStructureException
    {
        if (!questionPages.isEmpty() && questionPages.getFirst().hasConditions())
            throw new WrongDefinitionStructureException("First question in questionary is not allowed to have conditions.");

        for (Question<?> question : questions.values())
            question.checkDefinitionStructure();
    }

    public Question<?> getQuestion(@NonNull String questionId)
    {
        return questions.get(questionId);
    }

    public record QuestionPage(@NonNull Question<?> question, int pageIndex, boolean isFirst)
    {
        private QuestionPage(@NonNull Question<?> question, int pageIndex)
        {
            this(question, pageIndex, pageIndex==0);
        }
    }

    public QuestionPage getPage(@NonNull String questionId)
    {
        for (int i=0; i<questionPages.size(); i++)
        {
            Question<?> question = questionPages.get(i);
            if (questionId.equals(question.id))
                return new QuestionPage(question, i);
        }
        
        return null;
    }

    public QuestionPage getFirstPage()
    {
        if (questionPages.isEmpty())
            return null;

        return new QuestionPage(questionPages.getFirst(), 0, true);
    }

    public QuestionPage getNextPage(int pageIndex, @NonNull Direction direction, @NonNull QuestionaryAnswers questionaryAnswers)
    {
        switch (direction)
        {
        case PREV:
            for (int i=pageIndex-1; i>=0; i--)
            {
                Question<?> question = questionPages.get(i);
                if (question.isActive())
                    return new QuestionPage(question, i);
            }
            break;

        case NEXT:
            for (int i=pageIndex+1; i<questionPages.size(); i++)
            {
                Question<?> question = questionPages.get(i);
                if (question.isActive())
                    return new QuestionPage(question, i);
            }
            break;
        }

        return null;
    }
}
