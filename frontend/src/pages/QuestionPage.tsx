import type { JSX } from "react";
import type { Page } from "../types/PageTypes";
import type { PolymorphicValue } from "../types/Types";
import BoolInput from "../components/BoolInput";
import SingleChoiceInput from "../components/SingleChoiceInput";
import MultipleChoiceInput from "../components/MultipleChoiceInput";

type Props = {
    page: Page,
    page_data: PolymorphicValue[],
}

function createInputComponent(
    page: Page,
    page_data: PolymorphicValue[]
): JSX.Element
{
    switch (page.type)
    {
        case "BOOL":
            return (
                <BoolInput
                    questionId={page.id}
                    page_data={page_data}
                />
            );

        case "SINGLE":
            
            return (
                <SingleChoiceInput
                    questionId={page.id}
                    options={page.options}
                    page_data={page_data}
                />
            );

        case "MULTIPLE":
            return (
                <MultipleChoiceInput
                    questionId={page.id}
                    options={page.options}
                    page_data={page_data}
                />
            );
    }
}

export default function QuestionPage( { page, page_data }: Readonly<Props>): JSX.Element
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
            {createInputComponent(page, page_data)}
        </>
    );
}
