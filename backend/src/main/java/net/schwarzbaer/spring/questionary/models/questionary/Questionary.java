package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.PageDirection;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionaryDef;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;

public class Questionary
{
    @NonNull private final QuestionaryDef definition;
    @NonNull public  final String title;
    @NonNull private final Map<String,Question<?>> questions;
    @NonNull private final List<Question<?>> questionPages;

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
                question.dereferenceQuestionIdsInConditions(questions::get);
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
        if (title.isBlank())
            throw new WrongDefinitionStructureException("There is no title defined in questionary.");

        if (questionPages.isEmpty())
            throw new WrongDefinitionStructureException("There are no showable questions in questionary.");

        if (questionPages.getFirst().hasConditions())
            throw new WrongDefinitionStructureException("First question in questionary is not allowed to have conditions.");

        for (Question<?> question : questions.values())
            question.checkDefinitionStructure();
    }

    public Question<?> getQuestion(@NonNull String questionId)
    {
        return questions.get(questionId);
    }

    public QuestionPageInfo getPage(@NonNull String questionId)
    {
        for (int i=0; i<questionPages.size(); i++)
        {
            Question<?> question = questionPages.get(i);
            if (questionId.equals(question.id))
                return new QuestionPageInfo(question, i);
        }
        
        return null;
    }

    public QuestionPageInfo getFirstPage()
    {
        if (questionPages.isEmpty())
            return null;

        return new QuestionPageInfo(questionPages.getFirst(), 0, true);
    }

    public QuestionPageInfo getLastPage(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        return getNextPage(questionPages.size(), PageDirection.PREV, questionaryAnswers);
    }

    public QuestionPageInfo getNextPage(int pageIndex, @NonNull PageDirection direction, @NonNull QuestionaryAnswers questionaryAnswers)
    {
        switch (direction)
        {
        case PREV:
            pageIndex = Math.min(pageIndex, questionPages.size());
            for (int i=pageIndex-1; i>=0; i--)
            {
                Question<?> question = questionPages.get(i);
                if (question.isActive(questionaryAnswers))
                    return new QuestionPageInfo(question, i);
            }
            break;

        case NEXT:
            pageIndex = Math.max(pageIndex, -1);
            for (int i=pageIndex+1; i<questionPages.size(); i++)
            {
                Question<?> question = questionPages.get(i);
                if (question.isActive(questionaryAnswers))
                    return new QuestionPageInfo(question, i);
            }
            break;
        }

        return null;
    }

    public void forEachToplevelQuestion(Consumer<Question<?>> action)
    {
        definition.questions().forEach(def -> {
            Question<?> question = questions.get(def.getId());
            if (question!=null)
                action.accept(question);
        });
    }
}
