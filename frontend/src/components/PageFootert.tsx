import '../App.css';
import { type JSX } from 'react';

type Props = {
    prevBtnText: string,
    nextBtnText: string,
    isPrevBtnDisabled: boolean,
    isNextBtnDisabled: boolean,
    onClickPrevBtn: ()=>void,
    onClickNextBtn: ()=>void,
}

export default function PageFooter( props: Readonly<Props>): JSX.Element
{
    return (
        <>
            <hr/>
            <div>
                <button className={"PageButton"} onClick={props.onClickPrevBtn} disabled={props.isPrevBtnDisabled}>{props.prevBtnText}</button>
                <button className={"PageButton"} onClick={props.onClickNextBtn} disabled={props.isNextBtnDisabled}>{props.nextBtnText}</button>
            </div>
       </>
    );
}