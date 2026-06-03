import type { JSX } from "react";
import type { OptionDef } from "../types/DefinitionsTypes";
import "./AllQuestionResume.css";

type Props = {
    answers: OptionDef[],
}

export default function ChoiceQuestionResume( { answers }: Props ): JSX.Element
{
    return (
        <>
        {
            answers.length == 0
                ? <span className="NoAnswer">Es wurde nichts angegeben</span>
                : answers.map(
                    answer =>
                        <span key={answer.value} className="Answer">
                            {answer.label || answer.value}
                        </span>
                )
        }
        </>
    );
}
