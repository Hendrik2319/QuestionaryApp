import type { JSX } from "react";
import type { QuestionResumeDTO } from "../types/ResumeTypes";
import BoolQuestionResume from "../components/BoolQuestionResume";
import ChoiceQuestionResume from "../components/ChoiceQuestionResume";
import GroupQuestionResume from "../components/GroupQuestionResume";
import "./ResumePage.css";

type Props = {
    sessionId: string,
    questions: QuestionResumeDTO[],
}

export function createQuestionResumeList( questions: QuestionResumeDTO[] ): JSX.Element[]
{
    return questions.map(
        question => (
            <div key={question.id} className="Question">
                <div className="QuestionText">{question.text}</div>
                <div className="QuestionValues">{
                    question.active
                        ? createQuestionResumeComponent(question)
                        : <span className="NotRelevant">Ist hier nicht relevant im Kontext der anderen Fragen.</span>
                }</div>
            </div>
        )
    )
}

function createQuestionResumeComponent( question: QuestionResumeDTO ): JSX.Element
{
    switch (question.type)
    {
        case "BOOL"    : return <BoolQuestionResume answer={question.answer}/>;
        case "SINGLE"  : return <ChoiceQuestionResume answers={question.answers}/>;
        case "MULTIPLE": return <ChoiceQuestionResume answers={question.answers}/>;
        case "GROUP"   : return <GroupQuestionResume subQuestions={question.sub_questions}/>;
    }
}

export default function ResumePage( { sessionId, questions }: Props ): JSX.Element
{
    return (
        <>
            <h3>Zusammenfassung</h3>
            <div className="Resume">
                {createQuestionResumeList( questions )}
            </div>
        </>
    );
}
