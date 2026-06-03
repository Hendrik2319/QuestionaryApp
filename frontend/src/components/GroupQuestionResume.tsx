import type { JSX } from "react";
import type { QuestionResumeDTO } from "../types/ResumeTypes";
import { createQuestionResumeList } from "../pages/ResumePage";
import "./AllQuestionResume.css";

type Props = {
    subQuestions: QuestionResumeDTO[],
}

export default function GroupQuestionResume( { subQuestions }: Props ): JSX.Element
{
    return (
        <>
            {createQuestionResumeList(subQuestions)}
        </>
    );
}
