import { useState } from 'react';
import './App.css';
import UploadQuestionaryFile from './components/UploadQuestionaryFile';

function App() {
    // const [sessionId, setSessionId] = useState<String>("");
    const [title    , setTitle    ] = useState<String>("");
    // const title = 'Test';

    // function setTitle(title: string)
    // {
    //     console.debug('setTitle', { title });
    // }

    return (
        <>
            <h1>Questionary: {title ? title : "?????"}</h1>
            <UploadQuestionaryFile changeTitle={setTitle}/>
        </>
    )
}

export default App
