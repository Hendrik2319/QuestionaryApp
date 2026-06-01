import { useEffect, useState, type JSX } from 'react';
import './App.css';
import { BackendAPI } from './BackendAPI';
import UploadQuestionaryFile from './components/UploadQuestionaryFile';
import type { InitialValuesDTO } from './Types';

function App()
{
    const [sessionId , setSessionId ] = useState<null | String>(null);
    const [title     , setTitle     ] = useState<String>("????");
    const [needQuest , setNeedQuest ] = useState<boolean>(false);
    const [loading   , setLoading   ] = useState<boolean>(true);
    // const [loadingMsg, setLoadingMsg] = useState<LoadingMsg>({ message: "Load Initial Values", isLoading: true });

    useEffect(() => {
        if (!sessionId) {
            BackendAPI.fetchInitialData(
                "App.useEffect",
                (data: InitialValuesDTO) => {
                    setSessionId(data.session_id);
                    setNeedQuest(data.need_questionary);
                    setTitle(data.title || "????");
                    // setLoadingMsg({
                    //     message: "",
                    //     isLoading: false,
                    // });
                    setLoading(false);
                },
                (/* error */) => {
                    // setLoadingMsg({
                    //     message: "",
                    //     isLoading: false,
                    // });
                    setLoading(false);
                }
            );
        }
    }, []);

    let page: JSX.Element = (<span>Dummy Text</span>);

    if (loading)
    {
        return <div>Lade Session...</div>;
        //page = (<div>Loading ...</div>);
    }
    // if (loadingMsg.isLoading)
    // {
    //     page = (<div>{loadingMsg.message} ...</div>);
    // }
    else if (needQuest)
    {
        page = (<UploadQuestionaryFile changeTitle={(title)=>{ setTitle(title); setNeedQuest(false); }}/>);
    }

    return (
        <>
            <h1>Questionary: {title ? title : "?????"}</h1>
            <span>
                [{Math.random()}]<br/>
                sessionId:"{sessionId}"<br/>
                needQuest:{needQuest?"true":"false"}<br/>
                loading:{loading?"true":"false"}<br/>
            </span><br/>
            {page}
        </>
    )
}

export default App
