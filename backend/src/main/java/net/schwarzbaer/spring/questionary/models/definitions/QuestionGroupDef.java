package net.schwarzbaer.spring.questionary.models.definitions;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString(callSuper=true)
public class QuestionGroupDef extends QuestionDef
{
    private List<QuestionDef> subQuestions;
}
