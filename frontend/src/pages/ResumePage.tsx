import type { JSX } from "react";
import type { QuestionResumeDTO } from "../types/ResumeTypes";

type Props = {
    questions: QuestionResumeDTO[],
}

export default function ResumePage( _props: Readonly<Props>): JSX.Element
{
    return (
        <>
            <h3>Zusammenfasseung</h3>
        </>
    );
}
