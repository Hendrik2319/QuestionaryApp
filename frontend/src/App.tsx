import './App.css';
import UploadQuestionaryFile from './components/UploadQuestionaryFile';

function App() {
    // const [sessionId, setSessionId] = useState<String>("");
    // const [title    , setTitle    ] = useState<String>("");
    const title = 'Test';


    return (
        <>
            <h1>Questionary: {title ? title : "?????"}</h1>
            <UploadQuestionaryFile/>
        </>
    )
}

export default App
