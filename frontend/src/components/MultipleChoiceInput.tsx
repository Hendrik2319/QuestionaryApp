import { useEffect, useState, type ChangeEvent, type JSX } from "react";
import BackendAPI from "../BackendAPI";
import type { OptionDef } from "../types/DefinitionsTypes";
import type { PolymorphicValue } from "../types/Types";
import "./AllInput.css";

type Props = {
    sessionId: string,
    questionId: string,
    options: OptionDef[],
    answers: PolymorphicValue[],
}

function buildStringSet(answers: PolymorphicValue[]): Set<string>
{
    return new Set<string>(
        answers
            .map(polyVal => polyVal.type == "STRING" ? polyVal.value : null)
            .filter(str => str!=null)
    );
}

export default function MultipleChoiceInput( { sessionId, questionId, options, answers }: Props ): JSX.Element
{
    const [localAnswers, setLocalAnswers ] = useState<Set<string>>(buildStringSet(answers));
    
    useEffect(() => {
        setLocalAnswers( buildStringSet( answers ) );
    }, [answers]);
    
    function onChange( event: ChangeEvent<HTMLInputElement>, value: string )
    {
        const addValue: boolean = event.target.checked;

        BackendAPI.sendAnswer(
            "MultipleChoiceInput.onChange",
            sessionId,
            questionId,
            addValue ? "SET" : "UNSET",
            { type: "STRING", value },
        );

        if (addValue && !localAnswers.has(value))
        {
            const newAnswers = new Set<string>(localAnswers);
            newAnswers.add(value);
            setLocalAnswers( newAnswers );
        }
        else if (!addValue && localAnswers.has(value))
        {
            const newAnswers = new Set<string>(localAnswers);
            newAnswers.delete(value);
            setLocalAnswers( newAnswers );
        }
    }

    return (
        <div className="InputGroup">
        {
            options.map(
                (option: OptionDef) => {
                    const value: string = option.value;
                    const label: string = option.label || value;
                    return (
                        <label key={value}>
                            <input
                                type="checkbox"
                                checked={localAnswers.has(value)}
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
