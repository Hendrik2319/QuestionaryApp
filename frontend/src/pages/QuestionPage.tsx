import type { JSX } from "react";
import type { Page } from "../types/PageTypes";
import type { PolymorphicValue } from "../types/Types";
import BoolInput from "../components/BoolInput";
import SingleChoiceInput from "../components/SingleChoiceInput";
import MultipleChoiceInput from "../components/MultipleChoiceInput";
import "./QuestionPage.css";

type Props = {
    sessionId: string,
    page: Page,
    answers: PolymorphicValue[],
}

function createInputComponent(
    sessionId: string,
    page: Page,
    answers: PolymorphicValue[]
): JSX.Element
{
    switch (page.type)
    {
        case "BOOL":
            return (
                <BoolInput
                    sessionId={sessionId}
                    questionId={page.id}
                    answers={answers}
                />
            );

        case "SINGLE":
            
            return (
                <SingleChoiceInput
                    sessionId={sessionId}
                    questionId={page.id}
                    options={page.options}
                    answers={answers}
                />
            );

        case "MULTIPLE":
            return (
                <MultipleChoiceInput
                    sessionId={sessionId}
                    questionId={page.id}
                    options={page.options}
                    answers={answers}
                />
            );
    }
}

export default function QuestionPage( { sessionId, page, answers }: Readonly<Props>): JSX.Element
{
    const texts: string[] = page.texts;
    let content: JSX.Element = createInputComponent( sessionId, page, answers );
    for (let i=texts.length-1; i>=0; i--)
        content = (
            <div className="Question">
                <div className="QuestionText">{texts[i]}</div>
                {content}
            </div>
        );
    
    return (
        <div className="QuestionPage">
            {content}
        </div>
    );
}
