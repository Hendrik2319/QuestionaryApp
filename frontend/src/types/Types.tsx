export type QuestionaryTitle = {
    title: string,
}

export type LoadingMsg = {
    message: string,
    isLoading: boolean,
}

export type InitialValuesDTO = {
    session_id: string,
    need_questionary: boolean,
    title: null | string,
}

