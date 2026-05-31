package net.schwarzbaer.spring.questionary.models.questionary;

import java.util.function.Function;

import lombok.NonNull;
import net.schwarzbaer.spring.questionary.models.answers.QuestionAnswerValue;
import net.schwarzbaer.spring.questionary.models.definitions.BoolQuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.ChoiceQuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionValueDef;
import net.schwarzbaer.spring.questionary.models.definitions.ConditionsGroupDef;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionDef;
import net.schwarzbaer.spring.questionary.models.definitions.QuestionGroupDef;
import net.schwarzbaer.spring.questionary.models.definitions.SelectionType;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;

public abstract class Question<DefinitionType extends QuestionDef>
{
    protected final @NonNull DefinitionType definition;
    protected final QuestionGroup parentGroup;
    protected final ConditionsGroup conditions;
    public final String id;
    public final String text;
    public final @NonNull QuestionIndex index;

    protected Question(QuestionGroup parentGroup, @NonNull QuestionIndex index, @NonNull DefinitionType definition)
    {
        this.index = index;
        this.parentGroup = parentGroup;
        this.definition = definition;
        id   = this.definition.getId  ();
        text = this.definition.getText();

        ConditionsGroupDef conditionsGroupDef = definition.getConditions();
        conditions = conditionsGroupDef == null ? null : new ConditionsGroup(conditionsGroupDef);
    }

    static Question<?> createQuestion(QuestionGroup parentGroup, @NonNull QuestionIndex index, @NonNull QuestionDef questionDef)
    {
        if (questionDef instanceof BoolQuestionDef def)
            return new BoolQuestion(parentGroup, index, def);

        if (questionDef instanceof QuestionGroupDef def)
            return new QuestionGroup(parentGroup, index, def);

        if (questionDef instanceof ChoiceQuestionDef def)
            return new ChoiceQuestion(parentGroup, index, def);

        throw new IllegalArgumentException("Unknown sub type of QuestionDef");
    }

    void dereferenceIdsInConditions(@NonNull Function<String,Question<?>> findQuestion)
    {
        if (conditions!=null)
            conditions.dereferenceIds(findQuestion);
    }

    void checkDefinitionStructure() throws WrongDefinitionStructureException
    {
        if (text==null)
    		throw new WrongDefinitionStructureException(
                "Question (id:\"%s\") has no field \"text\""
                .formatted(id)
            );
        
        if (text.isBlank())
    		throw new WrongDefinitionStructureException(
                "Question (id:\"%s\") has a field \"text\" with empty or blank string"
                .formatted(id)
            );
        
        if (conditions!=null)
            conditions.checkDefinitionStructure(this);
    }

    abstract boolean meetToConditionValue(@NonNull ConditionValueDef value);
    public abstract boolean meetToAnswerValue(@NonNull QuestionAnswerValue value);

    public SelectionType getSelectionType()
    {
        return definition.getSelectionType();
    }
}
