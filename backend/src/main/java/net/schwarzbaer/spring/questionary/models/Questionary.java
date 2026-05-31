package net.schwarzbaer.spring.questionary.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionaryDef;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;

public class Questionary
{
    private final QuestionaryDef definition;
    private final Map<String,Question<?>> questions;
    private final @NonNull String title;

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
                {
                    group.forEachSubQuestion((QuestionGroup.ForEachSubQuestionAction) subQuestion -> addToMap(questions, subQuestion));
                }
            }

            for (Question<?> question : questions.values())
                question.dereferenceIdsInConditions(questions::get);
        }
    }

    private static void addToMap(Map<String,Question<?>> questionsMap, Question<?> question) throws WrongDefinitionStructureException
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
        for (Question<?> question : questions.values())
            question.checkDefinitionStructure();
    }
}
