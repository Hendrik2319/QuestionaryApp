import { useEffect, useState, type JSX } from 'react';
import './App.css';
import BackendAPI from './BackendAPI';
import PageFooter from './components/PageFootert';
import PageHeader from './components/PageHeader';
import ShowMessage from './components/ShowMessage';
import UploadQuestionaryFile from './components/UploadQuestionaryFile';
import { generateRandomString } from './Debug';
import QuestionPage from './pages/QuestionPage';
import ResumePage from './pages/ResumePage';
import type { GetPageResponseDTO, InitialValuesDTO } from './types/Types';

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

    function onClickPrevBtn()
    {
        // TODO
    }

    function onClickNextBtn()
    {
        // TODO
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
                <PageHeader title={title}/>
                {generateDebugInfo()}
                {content}
                <PageFooter
                    isPrevBtnDisabled={setAndGetStateValue( buttonOptions?.isPrevBtnDisabled, isPrevBtnDisabled, setPrevBtnDisabled )}
                    isNextBtnDisabled={setAndGetStateValue( buttonOptions?.isNextBtnDisabled, isNextBtnDisabled, setNextBtnDisabled )}
                    prevBtnText={setAndGetStateValue( buttonOptions?.prevBtnText, prevBtnText, setPrevBtnText )}
                    nextBtnText={setAndGetStateValue( buttonOptions?.nextBtnText, nextBtnText, setNextBtnText )}
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
        if (!pageData)
        {
            setLoadingMsg({ message:"Erste Frage wird geladen", isLoading: true });
            BackendAPI.getNextPage(
                "MainPage[Load first Page]",
                sessionId, null, "NEXT",
                responseDTO => {
                    setLoadingMsg({ message:"", isLoading: false });
                    setPageData(responseDTO);
                    console.debug("BackendAPI.getNextPage -> ", { responseDTO });
                }
            );
        }
        else
        {
            switch (pageData.type)
            {
                case 'PAGE':
                    return generatePage(
                        (<QuestionPage
                            page={pageData.page}
                            page_data={pageData.page_data}
                        />),
                        {
                            isPrevBtnDisabled: pageData.page.is_first,
                            isNextBtnDisabled: false,
                        }
                    );

                case 'RESUME':
                    return generatePage(
                        (<ResumePage
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
        <>
        </>
    );
}
