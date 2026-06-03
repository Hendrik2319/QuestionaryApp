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


interface Answer_Base {
    questionId: string,
}
interface BoolAnswer extends Answer_Base {
    type: "BOOL",
    answer: boolean,
}
interface SingleChoiceAnswer extends Answer_Base {
    type: "SINGLE",
    answer: string,
}
interface MultipleChoiceAnswer extends Answer_Base {
    type: "MULTIPLE",
    answers: string[],
}

export type Answer =
    | BoolAnswer
    | SingleChoiceAnswer
    | MultipleChoiceAnswer;

export type Download = {
    sessionId: string,
    answers: Answer[],
}