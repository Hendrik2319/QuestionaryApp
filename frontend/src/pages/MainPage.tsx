import { useEffect, useState, type JSX } from "react";
import { BackendAPI } from "../BackendAPI";
import ShowMessage from "../components/ShowMessage";
import UploadQuestionaryFile from "../components/UploadQuestionaryFile";
import { generateRandomString } from "../Debug";
import type { InitialValuesDTO } from "../types/Types";
import './MainPage.css';

type Props = {
    setTitle: (title: string) => void,
    setPrevBtnDisabled: (disabled: boolean) => void,
    setNextBtnDisabled: (disabled: boolean) => void,
    setPrevBtnText: (text: string) => void,
    setNextBtnText: (text: string) => void,
}

type LoadingMsg = {
    message: string,
    isLoading: boolean,
}

export default function MainPage( props: Readonly<Props>): JSX.Element
{
    const [sessionId , setSessionId ] = useState<null|String>(null);
    const [needQuest , setNeedQuest ] = useState<boolean>(false);
    const [loadingMsg, setLoadingMsg] = useState<LoadingMsg>({ message: "Load Initial Values", isLoading: true });
    const [message   , setMessage   ] = useState<null|string>(null);

    useEffect(() => {
        if (!sessionId) {
            BackendAPI.fetchInitialData(
                "MainPage.useEffect",
                (data: InitialValuesDTO) => {
                    setSessionId(data.session_id);
                    setNeedQuest(data.need_questionary);
                    if (data.title)
                        props.setTitle(data.title);
                    setLoadingMsg({ message: "", isLoading: false });
                },
                (/* error */) => {
                    setLoadingMsg({ message: "", isLoading: false });
                }
            );
        }
    }, []);

    function generateDebugInfo(): JSX.Element
    {
        return (
            <>
                <span className={"DebugInfo"}>
                    [{generateRandomString(7)}]<br/>
                    SessionId: "{sessionId}"<br/>
                    Need Quest: {needQuest?"true":"false"}<br/>
                    Loading Msg: {loadingMsg.isLoading?"true":"false"} / "{loadingMsg.message}"<br/>
                    Message: {message==null ? "<null>" : `\"${message}\"`}<br/>
                </span>
                <br/>
            </>
        );
    }

    if (message)
        return (
            <>
                {generateDebugInfo()}
                <ShowMessage
                    message={message}
                    onBtnClick={()=>setMessage(null)}
                />
            </>
        );

    if (loadingMsg.isLoading)
        return (
            <>
                {generateDebugInfo()}
                <div>{loadingMsg.message} ...</div>
            </>
        );

    if (needQuest)
        return (
            <>
                {generateDebugInfo()}
                <UploadQuestionaryFile
                    text={"Bitte wählen Sie eine Fragebogen-Datei aus und klicken Sie auf \"Hochladen\":"}
                    notifyLoading={isLoading => {
                        if (isLoading)
                            setLoadingMsg({ message:"Fragebogen wird hochgeladen", isLoading: true });
                    }}
                    setResult={result => {
                        setLoadingMsg({ message:"", isLoading:false });

                        if (result.questionaryTitle)
                            props.setTitle(result.questionaryTitle);

                        if (result.success)
                            setMessage("Hochladen war erfolgreich");
                        else
                            setMessage("Hochladen ist fehlgeschlagen (Details in Console)");

                        setNeedQuest(!result.success);
                    }}
                />
            </>
        );

    return (
        <>
            {generateDebugInfo()}
            <span>Dummy Text</span>
        </>
    );
}
