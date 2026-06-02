import type { AxiosResponse } from "axios";
import axios from "axios";
import type { GetPageRequestDTO, GetPageResponseDTO, InitialValuesDTO, PageDirection, QuestionaryTitle } from "./types/Types";

export const BackendAPI = {

    fetchInitialData: (
        callerLabel: string,
        handleResponseData: (response: InitialValuesDTO) => void,
        onError?: (error: any)=>void
    ) =>
        processPromise(
            axios.get( "/api/init" ),
            "fetchInitialData",
            "fetch initial data",
            callerLabel,
            handleResponseData,
            onError
        ),

    uploadQuestionaryFile: (
        callerLabel: string,
        fileContent: string,
        handleResponseData: (response: QuestionaryTitle) => void,
        onError?: (error: any)=>void
    ) =>
        processPromise(
            axios.post(
                "/api/questionary", fileContent,
                { headers: { "Content-Type": "text/plain", }, }
            ),
            "uploadQuestionaryFile",
            "setting questionary file",
            callerLabel,
            handleResponseData,
            onError
        ),
    
    getNextPage: (
        callerLabel: string,
        sessionId: string,
        questionId: string | null,
        direction: PageDirection,
        handleResponseData: (response: GetPageResponseDTO) => void,
        onError?: (error: any)=>void
    ) => {
        const requestDTO: GetPageRequestDTO =  questionId ? { question_id: questionId } : {};
        processPromise(
            axios.post( `/api/${sessionId}/page/${direction}`, requestDTO ),
            "getNextPage",
            "gtting next page",
            callerLabel,
            handleResponseData,
            onError
        )
    },
};

function processPromise<T>(
    promise: Promise<AxiosResponse>,
    functionLabel: string,
    whenText: string,
    callerLabel: string,
    handleResponseData: (responseData: T) => void,
    onError?: (error: any)=>void
) {
    const callText = `${callerLabel} -> BackendAPI.${functionLabel}`;
    console.debug(`[BackendAPI] ${callText} (${whenText})`);
    promise
        .then((response) => {
            if (response.status !== 200)
                throw new Error(`Get wrong response status, when ${whenText}: ${response.status}`);
            handleResponseData(response.data);
        })
        .catch((error) => {
            const label = `ERROR[ ${callText} ]`;
            const errorMessage = error?.response?.data;
            const message   = errorMessage?.error;
            const timestamp = errorMessage?.timestamp;
            if (message || timestamp)
                console.error(label, error, message && `\r\nCause:\r\n${message}`, timestamp && `\r\n\r\nTimestamp:\r\n${timestamp}`);
            else
                console.error(label, error);
            if (onError) onError( error );
        })
}
