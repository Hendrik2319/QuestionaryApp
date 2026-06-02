import type { Page } from "./PageTypes";
import type { QuestionResumeDTO } from "./ResumeTypes";

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
    page_data: PolymorphicValue[],
}
| {
    type: "RESUME",
    questions: QuestionResumeDTO[],
};

export type PageDirection = "PREV" | "NEXT";

export type PolymorphicValue =
| {
    type: "BOOL",
    value: boolean,
}
| {
    type: "STRING",
    value: string,
}
