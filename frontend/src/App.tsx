import { useEffect, useState, type JSX } from 'react';
import './App.css';
import PageFooter from './components/PageFootert';
import PageHeader from './components/PageHeader';
import ShowMessage from './components/ShowMessage';
import UploadQuestionaryFile from './components/UploadQuestionaryFile';
import type { InitialValuesDTO } from './types/Types';
import { generateRandomString } from './Debug';
import BackendAPI from './BackendAPI';

type LoadingMsg = {
    message: string,
    isLoading: boolean,
}

export default function App(): JSX.Element
{
    const [sessionId , setSessionId ] = useState<null|string>(null);
    const [needQuest , setNeedQuest ] = useState<boolean>(false);
    const [loadingMsg, setLoadingMsg] = useState<LoadingMsg>({ message: "Load Initial Values", isLoading: true });
    const [message   , setMessage   ] = useState<null|string>(null);

    const [title            , setTitle          ] = useState<string>("<nichts geladen>");
    const [isPrevBtnDisabled, setPrevBtnDisabled] = useState<boolean>(true);
    const [isNextBtnDisabled, setNextBtnDisabled] = useState<boolean>(true);
    const [prevBtnText      , setPrevBtnText    ] = useState<string>("<");
    const [nextBtnText      , setNextBtnText    ] = useState<string>(">");

    useEffect(() => {
        if (!sessionId) {
            BackendAPI.fetchInitialData(
                "MainPage.useEffect",
                (data: InitialValuesDTO) => {
                    setSessionId(data.session_id);
                    setNeedQuest(data.need_questionary);
                    if (data.title)
                        setTitle(data.title);
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

    function onClickPrevBtn()
    {
        // TODO
    }

    function onClickNextBtn()
    {
        // TODO
    }

    function generatePage( content: JSX.Element ): JSX.Element
    {
        return (
            <>
                <PageHeader title={title}/>
                {generateDebugInfo()}
                {content}
                <PageFooter
                    isPrevBtnDisabled={isPrevBtnDisabled}
                    isNextBtnDisabled={isNextBtnDisabled}
                    prevBtnText={prevBtnText}
                    nextBtnText={nextBtnText}
                    onClickPrevBtn={onClickPrevBtn}
                    onClickNextBtn={onClickNextBtn}
                />
            </>
        );
    }

    if (message)
        return generatePage(
            <ShowMessage
                message={message}
                onBtnClick={()=>setMessage(null)}
            />
        );

    if (loadingMsg.isLoading)
        return generatePage(
            <div>{loadingMsg.message} ...</div>
        );

    if (needQuest)
        return generatePage(
            <UploadQuestionaryFile
                text={"Bitte wählen Sie eine Fragebogen-Datei aus und klicken Sie auf \"Hochladen\":"}
                notifyLoading={isLoading => {
                    if (isLoading)
                        setLoadingMsg({ message:"Fragebogen wird hochgeladen", isLoading: true });
                }}
                setResult={result => {
                    setLoadingMsg({ message:"", isLoading:false });

                    if (result.questionaryTitle)
                        setTitle(result.questionaryTitle);

                    if (result.success)
                        setMessage("Hochladen war erfolgreich");
                    else
                        setMessage("Hochladen ist fehlgeschlagen (Details in Console)");

                    setNeedQuest(!result.success);
                }}
            />
        );
    
    if (sessionId)
    {
        BackendAPI.getNextPage(
            "MainPage[TEST]",
            sessionId, null, "NEXT",
            responseDTO => {
                console.debug("BackendAPI.getNextPage -> ", { responseDTO });
            }
        );
    }
    
    return generatePage(
        <>
        </>
    );
}
