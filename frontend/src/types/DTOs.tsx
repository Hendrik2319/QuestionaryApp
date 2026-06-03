import type { Page } from "./PageTypes";
import type { QuestionResumeDTO } from "./ResumeTypes";
import type { PolymorphicValue } from "./Types";

export type QuestionaryTitle = {
    title: string,
};

export type InitialValuesDTO = {
    session_id: string,
    need_questionary: boolean,
    title: null | string,
};

export type GetPageRequestDTO = {
    question_id?: String  // no questionId means: get first/last page/question
};

export type GetPageResponseDTO = 
| {
    type: "PAGE",
    page: Page,
    answers: PolymorphicValue[],
}
| {
    type: "RESUME",
    questions: QuestionResumeDTO[],
};
