import type { JSX } from "react";
import type { Page } from "../types/PageTypes";
import type { PolymorphicValue } from "../types/Types";
import BoolInput from "../components/BoolInput";
import SingleChoiceInput from "../components/SingleChoiceInput";
import MultipleChoiceInput from "../components/MultipleChoiceInput";

type Props = {
    page: Page,
    answers: PolymorphicValue[],
}

function createInputComponent(
    page: Page,
    answers: PolymorphicValue[]
): JSX.Element
{
    switch (page.type)
    {
        case "BOOL":
            return (
                <BoolInput
                    questionId={page.id}
                    answers={answers}
                />
            );

        case "SINGLE":
            
            return (
                <SingleChoiceInput
                    questionId={page.id}
                    options={page.options}
                    answers={answers}
                />
            );

        case "MULTIPLE":
            return (
                <MultipleChoiceInput
                    questionId={page.id}
                    options={page.options}
                    answers={answers}
                />
            );
    }
}

export default function QuestionPage( { page, answers }: Readonly<Props>): JSX.Element
{
    const texts: string[] = page.texts;
    let textRows: JSX.Element = (<></>);
    for (let i=texts.length-1; i>=0; i--)
        textRows = (
            <div>
                <span className="TextRow">{texts[i]}</span>
                {textRows}
            </div>
        );
    
    return (
        <>
            {textRows}
            {createInputComponent(page, answers)}
        </>
    );
}
