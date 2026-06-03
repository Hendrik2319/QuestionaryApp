import { useEffect, useState, type ChangeEvent, type JSX } from "react";
import BackendAPI from "../BackendAPI";
import type { OptionDef } from "../types/DefinitionsTypes";
import type { PolymorphicValue } from "../types/Types";
import "./SingleChoiceInput.css";

type Props = {
    sessionId: string,
    questionId: string,
    options: OptionDef[],
    answers: PolymorphicValue[],
}

function buildAnswer(answers: PolymorphicValue[]): string|null
{
    for (const answer of answers)
        if (answer.type == "STRING")
            return answer.value;
    return null;
}

export default function SingleChoiceInput( { sessionId, questionId, options, answers }: Readonly<Props>): JSX.Element
{
    const [localAnswer, setLocalAnswer ] = useState<string|null>( buildAnswer(answers) );
    
    useEffect(() => {
        setLocalAnswer( buildAnswer(answers) );
    }, [answers]);

    function onChange( event: ChangeEvent<HTMLInputElement>, value: string )
    {
        if (!event.target.checked) return;
        
        BackendAPI.sendAnswer(
            "SingleChoiceInput.onChange",
            sessionId,
            questionId,
            "SET",
            { type: "STRING", value },
        );

        setLocalAnswer( value );
    }

    return (
        <div className="SingleChoiceInput">
        {
            options.map(
                (option: OptionDef) => {
                    const value: string = option.value;
                    const label: string = option.label || value;
                    return (
                        <label key={value}>
                            <input
                                name="SingleChoiceInput"
                                type="radio"
                                checked={localAnswer != null && value == localAnswer}
                                onChange={ev => onChange(ev,value)}
                            />
                            {label}
                        </label>
                    );
                }
            )
        }
        </div>
    );
}
