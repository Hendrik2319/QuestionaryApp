package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.PolymorphicValue;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionGroupDef;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;
import net.schwarzbaer.spring.questionary.models.page.Page;
import net.schwarzbaer.spring.questionary.models.resume.QuestionGroupResumeDTO;
import net.schwarzbaer.spring.questionary.models.resume.QuestionResumeDTO;

public class QuestionGroup extends Question<QuestionGroupDef>
{
    @NonNull private final List<Question<?>> subQuestions;

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
    void dereferenceQuestionIdsInConditions(@NonNull Function<String, Question<?>> findQuestion)
    {
        super.dereferenceQuestionIdsInConditions(findQuestion);

        for (Question<?> subQuestion : subQuestions)
            subQuestion.dereferenceQuestionIdsInConditions(findQuestion);
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
    public boolean meetsToValue(PolymorphicValue value)
    {
        throw new UnsupportedOperationException("A question group has no values.");
    }

    @Override
    public @NonNull Page createPage(boolean isFirst)
    {
        throw new UnsupportedOperationException("There not page for a whole question group.");
    }

    @Override
    public @NonNull QuestionGroupResumeDTO createResumeDTO(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        return new QuestionGroupResumeDTO(id, text, isActive(questionaryAnswers), getSubQuestionDTOs(questionaryAnswers));
    }

    private @NonNull List<QuestionResumeDTO> getSubQuestionDTOs(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        return subQuestions==null
            ? List.of()
            : subQuestions
                .stream()
                .map(q -> q.createResumeDTO(questionaryAnswers))
                .toList();
    }
}
