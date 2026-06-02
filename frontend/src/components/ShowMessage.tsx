import type { JSX } from "react";

type Props = {
    message: string,
    onBtnClick: () => void,
}

export default function ShowMessage( { message, onBtnClick }: Readonly<Props>): JSX.Element
{
    return (
        <>
            <span>{message}</span>
            &nbsp;&nbsp;
            <button onClick={onBtnClick}>Ok</button>
        </>
    );
}