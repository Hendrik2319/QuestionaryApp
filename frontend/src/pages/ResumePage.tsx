import type { JSX } from "react";
import BoolQuestionResume from "../components/BoolQuestionResume";
import ChoiceQuestionResume from "../components/ChoiceQuestionResume";
import GroupQuestionResume from "../components/GroupQuestionResume";
import type { Answer, Download, QuestionResumeDTO } from "../types/ResumeTypes";
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

function insertActiveAnswers( answers: Answer[], questions: QuestionResumeDTO[] ) {
    for (const question of questions)
    {
        if (question.active)
        {
            switch (question.type)
            {
                case "BOOL":
                    if (question.answer != null)
                        answers.push({
                            type: "BOOL",
                            questionId: question.id,
                            answer: question.answer,
                        });
                    break;

                case "SINGLE":
                    if (question.answers.length>0)
                        answers.push({
                            type: "SINGLE",
                            questionId: question.id,
                            answer: question.answers[0].value,
                        });
                    break;
                
                case "MULTIPLE":
                    if (question.answers.length>0)
                        answers.push({
                            type: "MULTIPLE",
                            questionId: question.id,
                            answers: question.answers.map( a => a.value ),
                        });
                    break;

                case "GROUP":
                    insertActiveAnswers( answers, question.sub_questions );
                    break;
            }
        }
    }
}

function buildDownload( sessionId: string, questions: QuestionResumeDTO[] ): Download
{
    const answers: Answer[] = [];
    insertActiveAnswers(answers, questions);
    return { sessionId, answers, };
}

export default function ResumePage( { sessionId, questions }: Props ): JSX.Element
{
    const download: Download = buildDownload(sessionId, questions);
    const json: string = JSON.stringify( download, null, 2);
    const blob: Blob   = new Blob([json], { type: "application/json" });
    const url : string = URL.createObjectURL(blob);
    return (
        <>
            <h3>Zusammenfassung</h3>
            <div className="Resume">
                {createQuestionResumeList( questions )}
                <div className="Download">
                    <a href={url} download={"answers_"+sessionId+".json"}>Hier</a>
                    &nbsp;die obigen Eingaben als Download.
                </div>
            </div>
        </>
    );
}
