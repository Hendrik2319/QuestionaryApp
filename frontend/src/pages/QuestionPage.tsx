import type { JSX } from "react";
import type { Page } from "../types/PageTypes";
import type { PolymorphicValue } from "../types/Types";

type Props = {
    page: Page,
    page_data: PolymorphicValue[],
}

export default function QuestionPage( props: Readonly<Props>): JSX.Element
{
    const texts: string[] = props.page.texts;
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
        </>
    );
}
