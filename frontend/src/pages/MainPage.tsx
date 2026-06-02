import { useEffect, useState, type JSX } from "react";
import type { InitialValuesDTO, LoadingMsg } from "../types/Types";
import { BackendAPI } from "../BackendAPI";
import UploadQuestionaryFile from "../components/UploadQuestionaryFile";

type Props = {
    setTitle: (title: string) => void,
    setPrevBtnDisabled: (disabled: boolean) => void,
    setNextBtnDisabled: (disabled: boolean) => void,
    setPrevBtnText: (text: string) => void,
    setNextBtnText: (text: string) => void,
}

function MainPage( props: Readonly<Props>): JSX.Element
{
    const [sessionId , setSessionId ] = useState<null | String>(null);
    const [needQuest , setNeedQuest ] = useState<boolean>(false);
    // const [loading   , setLoading   ] = useState<boolean>(true);
    const [loadingMsg, setLoadingMsg] = useState<LoadingMsg>({ message: "Load Initial Values", isLoading: true });

    useEffect(() => {
        if (!sessionId) {
            BackendAPI.fetchInitialData(
                "App.useEffect",
                (data: InitialValuesDTO) => {
                    setSessionId(data.session_id);
                    setNeedQuest(data.need_questionary);
                    props.setTitle(data.title || "????");
                    setLoadingMsg({
                        message: "",
                        isLoading: false,
                    });
                    // setLoading(false);
                },
                (/* error */) => {
                    setLoadingMsg({
                        message: "",
                        isLoading: false,
                    });
                    // setLoading(false);
                }
            );
        }
    }, []);

    function generateDebugInfo(): JSX.Element
    {
        return (
            <>
                <span>
                    [{Math.random()}]<br/>
                    sessionId:"{sessionId}"<br/>
                    needQuest:{needQuest?"true":"false"}<br/>
                    {/* loading:{loading?"true":"false"}<br/> */}
                    loadingMsg:{loadingMsg.isLoading?"true":"false"}/"{loadingMsg.message}"<br/>
                </span>
                <br/>
            </>
        );
    }

    // console.debug({ loadingMsg });

    // if (loading)
    // {
    //     return <div>Lade Session...</div>;
    //     //page = (<div>Loading ...</div>);
    // }
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
                    changeTitle={(title)=>{ props.setTitle(title); setNeedQuest(false); }}
                    setLoadingMsg={(msg)=>{ setLoadingMsg(msg); console.debug('UploadQuestionaryFile', { loadingMsg: msg }) }}
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

export default MainPage