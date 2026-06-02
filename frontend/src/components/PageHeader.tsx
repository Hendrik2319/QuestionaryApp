import '../App.css';
import { type JSX } from 'react';

type Props = {
    title: string,
}

export default function PageHeader( { title }: Readonly<Props>): JSX.Element
{
    return (
        <>
            <h1 className={"PageTitle"}>Fragebogen: {title}</h1>
            <hr/>
        </>
    );
}
