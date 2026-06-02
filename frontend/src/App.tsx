import { useState, type JSX } from 'react';
import './App.css';
import MainPage from './pages/MainPage';

function App(): JSX.Element
{
    const [title            , setTitle          ] = useState<string>("<no questionary loaded>");
    const [isPrevBtnDisabled, setPrevBtnDisabled] = useState<boolean>(true);
    const [isNextBtnDisabled, setNextBtnDisabled] = useState<boolean>(true);
    const [prevBtnText      , setPrevBtnText    ] = useState<string>("<");
    const [nextBtnText      , setNextBtnText    ] = useState<string>(">");

    function onClickPrevBtn()
    {
        // TODO
    }

    function onClickNextBtn()
    {
        // TODO
    }

    return (
        <>
            <h1 className={"PageTitle"}>Questionary: {title}</h1>
            <hr/>
            <MainPage
                setTitle={setTitle}
                setPrevBtnDisabled={setPrevBtnDisabled}
                setNextBtnDisabled={setNextBtnDisabled}
                setPrevBtnText={setPrevBtnText}
                setNextBtnText={setNextBtnText}                
            />
            <hr/>
            <div>
                <button className={"PageButton"} onClick={onClickPrevBtn} disabled={isPrevBtnDisabled}>{prevBtnText}</button>
                <button className={"PageButton"} onClick={onClickNextBtn} disabled={isNextBtnDisabled}>{nextBtnText}</button>
            </div>
        </>
    )
}

export default App
