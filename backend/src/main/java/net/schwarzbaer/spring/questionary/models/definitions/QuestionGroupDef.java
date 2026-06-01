package net.schwarzbaer.spring.questionary.models.definitions;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import net.schwarzbaer.spring.questionary.models.answers.QuestionaryAnswers;
import net.schwarzbaer.spring.questionary.models.resume.QuestionDefDTO;
import net.schwarzbaer.spring.questionary.models.resume.QuestionGroupDefDTO;

@Setter @Getter @ToString(callSuper=true)
public class QuestionGroupDef extends QuestionDef
{
    private List<QuestionDef> subQuestions;

    public QuestionGroupDef()
    {
        super(null);
    }

    private @NonNull List<QuestionDefDTO> getSubQuestionDTOs(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        return subQuestions==null
            ? List.of()
            : subQuestions
                .stream()
                .map(q -> q.createDTOForResume(questionaryAnswers))
                .toList();
    }

    @Override
    public QuestionGroupDefDTO createDTOForResume(@NonNull QuestionaryAnswers questionaryAnswers)
    {
        return new QuestionGroupDefDTO(getId(), getText(), getSubQuestionDTOs(questionaryAnswers));
    }
}
