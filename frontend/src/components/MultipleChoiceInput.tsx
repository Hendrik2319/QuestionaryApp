import type { JSX } from "react";
import BackendAPI from "../BackendAPI";
import type { OptionDef } from "../types/DefinitionsTypes";
import type { PolymorphicValue } from "../types/Types";

type Props = {
    sessionId: string,
    questionId: string,
    options: OptionDef[],
    answers: PolymorphicValue[],
}

function onChange(
    sessionId: string,
    questionId: string,
    checked: boolean,
    value:string,
)
{
    BackendAPI.sendAnswer(
        "MultipleChoiceInput.onChange",
        sessionId,
        questionId,
        checked ? "SET" : "UNSET",
        { type: "STRING", value },
    );
}

function wasAnswered(value:string, answers: PolymorphicValue[]): boolean
{
    for (const answer of answers)
        if (answer.type === "STRING" && answer.value === value)
            return true;
    return false;
}

export default function MultipleChoiceInput( { sessionId, questionId, options, answers }: Props): JSX.Element
{
    const inputs: JSX.Element[] = options
        .map((option: OptionDef) => {
            const value:string = option.value;
            const label:string = option.label || value;
            return (
                <label key={value}>
                    <input
                        type="checkbox"
                        checked={wasAnswered( value, answers )}
                        onChange={ev => onChange( sessionId, questionId, ev.target.checked, value )}
                    />
                    {label}
                </label>
            );
        });

    return (
        <div>
            {inputs}
        </div>
    );
}
