import type { PolymorphicValue } from "./Types";

export type ChangeType = "SET" | "UNSET";

export type SetAnswerDTO = {
    question_id: string,
    change_type: ChangeType,
    answer_value: PolymorphicValue,
};