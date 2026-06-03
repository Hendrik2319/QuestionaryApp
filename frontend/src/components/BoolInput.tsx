import { useEffect, useState, type ChangeEvent, type JSX } from "react";
import BackendAPI from "../BackendAPI";
import type { PolymorphicValue } from "../types/Types";
import "./AllInput.css";

type Props = {
    sessionId: string,
    questionId: string,
    answers: PolymorphicValue[],
}

function buildAnswer(answers: PolymorphicValue[]): boolean|null
{
    for (const answer of answers)
        if (answer.type == "BOOL")
            return answer.value;
    return null;
}

export default function BoolInput( { sessionId, questionId, answers }: Props): JSX.Element
{
    const [localAnswer, setLocalAnswer ] = useState<boolean|null>( buildAnswer(answers) );
    
    useEffect(() => {
        setLocalAnswer( buildAnswer(answers) );
    }, [answers]);

    function onChange( event: ChangeEvent<HTMLInputElement>, value: boolean )
    {
        if (!event.target.checked) return;
        
        BackendAPI.sendAnswer(
            "SingleChoiceInput.onChange",
            sessionId,
            questionId,
            "SET",
            { type: "BOOL", value },
        );

        setLocalAnswer( value );
    }

    return (
        <div className="InputGroup">
            <label>
                <input
                    name="BoolInput"
                    type="radio"
                    checked={localAnswer != null && localAnswer}
                    onChange={ev => onChange(ev,true)}
                />
                Ja
            </label>
            <label>
                <input
                    name="BoolInput"
                    type="radio"
                    checked={localAnswer != null && !localAnswer}
                    onChange={ev => onChange(ev,false)}
                />
                Nein
            </label>
        </div>
    );
}
