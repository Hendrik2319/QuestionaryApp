package net.schwarzbaer.spring.questionary.models.definitions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.schwarzbaer.spring.questionary.models.resume.BoolQuestionDefDTO;

@Setter @Getter @ToString(callSuper=true)
public class BoolQuestionDef extends QuestionDef
{
    public BoolQuestionDef()
    {
        super(SelectionType.Single);
    }
    
    @Override
    public BoolQuestionDefDTO createDTOForResume()
    {
        return new BoolQuestionDefDTO(getId(), getText());
    }
}
