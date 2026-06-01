import type { AxiosResponse } from "axios";
import axios from "axios";
import type { InitialValuesDTO, QuestionaryTitle } from "./Types";

export const BackendAPI = {

    fetchInitialData: (
        callerLabel: string,
        handleResponseData: (response: InitialValuesDTO) => void,
        onError?: (error: any)=>void
    ) =>
        processPromise(
            axios.get( "/api/getinit" ),
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
                "/api/setquestionary", fileContent,
                { headers: { "Content-Type": "text/plain", }, }
            ),
            "uploadQuestionaryFile",
            "setting questionary file",
            callerLabel,
            handleResponseData,
            onError
        ),
};

function processPromise<T>(
    promise: Promise<AxiosResponse>,
    functionLabel: string,
    whenText: string,
    callerLabel: string,
    handleResponseData: (responseData: T) => void,
    onError?: (error: any)=>void
) {
    promise
        .then((response) => {
            if (response.status !== 200)
                throw new Error("Get wrong response status, when " + whenText + ": " + response.status);
            handleResponseData(response.data);
        })
        .catch((error) => {
            const label = "ERROR[ " + callerLabel + " -> BackendAPI." + functionLabel + " ]";
            const errorMessage = error?.response?.data;
            const message   = errorMessage?.error;
            const timestamp = errorMessage?.timestamp;
            if (message || timestamp)
                console.error(label, error, "\r\nCause:\r\n"+ message, "\r\n\r\nTimestamp:\r\n"+timestamp);
            else
                console.error(label, error);
            if (onError) onError( error );
        })
}
