import type { OptionDef } from "./DefinitionsTypes";

interface QuestionResumeDTO_Base {
    id: string,
    text: string,
    active: boolean,
}

interface BoolQuestionResumeDTO extends QuestionResumeDTO_Base {
    type: "BOOL",
    answer: boolean | null;
}

interface ChoiceQuestionResumeDTO extends QuestionResumeDTO_Base {
    type: "SINGLE" | "MULTIPLE",
    answers: OptionDef[],
}

interface QuestionGroupResumeDTO extends QuestionResumeDTO_Base {
    type: "GROUP",
    sub_questions: QuestionResumeDTO[],
}

export type QuestionResumeDTO =
    | BoolQuestionResumeDTO
    | ChoiceQuestionResumeDTO
    | QuestionGroupResumeDTO;
