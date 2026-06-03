import type { JSX } from "react";
import "./AllQuestionResume.css";

type Props = {
    answer: boolean | null;
}

export default function BoolQuestionResume( { answer }: Props ): JSX.Element
{
    return (
        <>
        {
            answer == null
                ? <span className="NoAnswer">Es wurde nichts angegeben.</span>
                : <span className="Answer">{answer ? "Ja" : "Nein"}</span>
        }
        </>
    );
}
