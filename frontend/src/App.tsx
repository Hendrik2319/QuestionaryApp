import { useEffect, useState, type JSX } from 'react';
import './App.css';
import BackendAPI from './BackendAPI';
import ShowMessage from './components/ShowMessage';
import UploadQuestionaryFile from './components/UploadQuestionaryFile';
import { generateRandomString } from './Debug';
import QuestionPage from './pages/QuestionPage';
import ResumePage from './pages/ResumePage';
import type { GetPageResponseDTO, InitialValuesDTO, PageDirection } from './types/Types';

type LoadingMsg = {
    message: string,
    isLoading: boolean,
}

type ButtonOptions = {
    isPrevBtnDisabled?: boolean,
    isNextBtnDisabled?: boolean,
    prevBtnText?: string
    nextBtnText?: string
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

    const [pageData, setPageData] = useState<GetPageResponseDTO | null>(null);
    

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

    function gotoNextPage(sessionId: string, questionId: string | null, direction: PageDirection, loadingMsgText: string)
    {
        setLoadingMsg({ message:loadingMsgText, isLoading: true });
        BackendAPI.getNextPage(
            "MainPage.gotoNextPage",
            sessionId, questionId, direction,
            responseDTO => {
                setLoadingMsg({ message:"", isLoading: false });
                setPageData(responseDTO);
                console.debug("BackendAPI.getNextPage -> ", { sessionId, questionId, direction, loadingMsgText, responseDTO });
            }
        );
    }

    function onClickBtn(direction: PageDirection)
    {
        if (!sessionId) return;
        if (!pageData) return;
        gotoNextPage(
            sessionId,
            (pageData.type === 'PAGE' ? pageData.page.id : null),
            direction,
            "Frage wird geladen"
        );
    }

    function setAndGetStateValue<T = boolean | string>( newValue: T | undefined, oldValue: T, setFunc: (val:T)=>void ): T
    {
        if (newValue!==undefined && newValue!==oldValue)
        {
            setFunc(newValue);
            return newValue;
        }
        return oldValue;
    }

    function generatePage( content: JSX.Element, buttonOptions?: ButtonOptions ): JSX.Element
    {
        return (
            <>
                <h1 className={"PageTitle"}>Fragebogen: {title}</h1>
                <hr/>
                {generateDebugInfo()}
                {content}
                <hr/>
                <div>
                    <button
                        className={"PageButton"}
                        onClick ={()=>onClickBtn('PREV')}
                        disabled={setAndGetStateValue( buttonOptions?.isPrevBtnDisabled, isPrevBtnDisabled, setPrevBtnDisabled )}>
                                 {setAndGetStateValue( buttonOptions?.prevBtnText      , prevBtnText      , setPrevBtnText     )}
                    </button>
                    <button
                        className={"PageButton"}
                        onClick ={()=>onClickBtn('NEXT')}
                        disabled={setAndGetStateValue( buttonOptions?.isNextBtnDisabled, isNextBtnDisabled, setNextBtnDisabled )}>
                                 {setAndGetStateValue( buttonOptions?.nextBtnText      , nextBtnText      , setNextBtnText     )}
                    </button>
                </div>
            </>
        );
    }

    if (message)
        return generatePage(
            (<ShowMessage
                message={message}
                onBtnClick={()=>setMessage(null)}
            />),
            {
                isPrevBtnDisabled: true,
                isNextBtnDisabled: true,
            }
        );

    if (loadingMsg.isLoading)
        return generatePage(
            (<div>{loadingMsg.message} ...</div>),
            {
                isPrevBtnDisabled: true,
                isNextBtnDisabled: true,
            }
        );

    if (needQuest)
        return generatePage(
            (<UploadQuestionaryFile
                text={"Bitte wählen Sie eine Fragebogen-Datei aus und klicken Sie dann auf \"Hochladen\":"}
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
            />),
            {
                isPrevBtnDisabled: true,
                isNextBtnDisabled: true,
            }
        );
    
    if (sessionId)
    {
        if (!pageData)
        {
            gotoNextPage(
                sessionId,
                null,
                "NEXT",
                "Erste Frage wird geladen"
            );
        }
        else
        {
            switch (pageData.type)
            {
                case 'PAGE':
                    return generatePage(
                        (<QuestionPage
                            sessionId={sessionId}
                            page={pageData.page}
                            answers={pageData.answers}
                        />),
                        {
                            isPrevBtnDisabled: pageData.page.first_page,
                            isNextBtnDisabled: false,
                        }
                    );

                case 'RESUME':
                    return generatePage(
                        (<ResumePage
                            sessionId={sessionId}
                            questions={pageData.questions}
                        />),
                        {
                            isPrevBtnDisabled: false,
                            isNextBtnDisabled: true ,
                        }
                    );
            }
        }
    }
    
    return generatePage(
        (<></>),
        {
            isPrevBtnDisabled: true,
            isNextBtnDisabled: true,
        }
    );
}
