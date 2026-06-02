import type { JSX } from "react";
import type { PolymorphicValue } from "../types/Types";
import type { OptionDef } from "../types/DefinitionsTypes";

type Props = {
    questionId: string,
    options: OptionDef[],
    page_data: PolymorphicValue[],
}

export default function SingleChoiceInput( _props: Readonly<Props>): JSX.Element
{
    return (
        <>
        </>
    );
}
