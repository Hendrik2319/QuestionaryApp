package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.answers.QuestionAnswerValue;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionValueDef;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionGroupDef;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;

public class QuestionGroup extends Question<QuestionGroupDef>
{
    private final List<Question<?>> subQuestions;

    public QuestionGroup(QuestionGroup parentGroup, @NonNull QuestionIndex index, @NonNull QuestionGroupDef definition)
    {
        super(parentGroup, index, definition);

        subQuestions = new ArrayList<>();
        List<QuestionDef> questionDefs = this.definition.getSubQuestions();
        if (questionDefs != null && !questionDefs.isEmpty())
        {
            for (int i=0; i<questionDefs.size(); i++)
            {
                Question<?> subQuestion = Question.createQuestion(this, index.createSubIndex(i), questionDefs.get(i));
                subQuestions.add(subQuestion);
            }
        }
    }

    public void forEachSubQuestion(@NonNull Consumer<Question<?>> action)
    {
        for (Question<?> subQuestion : subQuestions)
            action.accept(subQuestion);
    }

    public void forEachSubQuestion(@NonNull ForEachSubQuestionAction action) throws WrongDefinitionStructureException
    {
        for (Question<?> subQuestion : subQuestions)
            action.accept(subQuestion);
    }

    interface ForEachSubQuestionAction
    {
        void accept(Question<?> subQuestion) throws WrongDefinitionStructureException;
    }

    @Override
    void dereferenceIdsInConditions(@NonNull Function<String, Question<?>> findQuestion)
    {
        super.dereferenceIdsInConditions(findQuestion);

        for (Question<?> subQuestion : subQuestions)
            subQuestion.dereferenceIdsInConditions(findQuestion);
    }

    @Override
    void checkDefinitionStructure() throws WrongDefinitionStructureException
    {
        super.checkDefinitionStructure();

        for (Question<?> subQuestion : subQuestions)
        {
            if (subQuestion instanceof QuestionGroup)
                throw new WrongDefinitionStructureException(
                    "A QuestionGroup (id\"%s\") cannot be part of another QuestionGroup (id\"%s\")."
                    .formatted(subQuestion.id, id)
                );
            
            subQuestion.checkDefinitionStructure();
        }
    }

    @Override
    boolean meetToConditionValue(ConditionValueDef value)
    {
        throw new IllegalStateException("No condition can't refer directly to a question group.");
    }

    @Override
    public boolean meetToAnswerValue(QuestionAnswerValue value)
    {
        throw new IllegalStateException("No answer can't target directly to a question group.");
    }
}
