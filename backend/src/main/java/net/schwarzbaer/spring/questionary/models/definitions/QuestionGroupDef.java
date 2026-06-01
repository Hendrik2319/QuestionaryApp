package net.schwarzbaer.spring.questionary.models.definitions;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.resume.QuestionResumeDTO;
import net.schwarzbaer.spring.questionary.models.resume.QuestionGroupResumeDTO;

@Setter @Getter @ToString(callSuper=true)
public class QuestionGroupDef extends QuestionDef
{
    private List<QuestionDef> subQuestions;

    public QuestionGroupDef()
    {
        super(null);
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

    @Override
    public QuestionGroupResumeDTO createResumeDTO(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        return new QuestionGroupResumeDTO(getId(), getText(), getSubQuestionDTOs(questionaryAnswers));
    }
}
