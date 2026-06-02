import type { JSX } from "react";
import type { PolymorphicValue } from "../types/Types";

type Props = {
    sessionId: string,
    questionId: string,
    answers: PolymorphicValue[],
}

export default function BoolInput( _props: Readonly<Props>): JSX.Element
{
    return (
        <>
        </>
    );
}
