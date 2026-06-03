import type { ChangeEvent, JSX } from "react";
import { useState } from "react";
import BackendAPI from "../BackendAPI";
import type { QuestionaryTitle } from "../types/DTOs";
import "./UploadQuestionaryFile.css";

type Props = {
    text: string,
    notifyLoading: (isLoading: boolean) => void,
    setResult: (result: Result) => void,
};

export type Result = {
    success: boolean,
    questionaryTitle?: string,
};

export default function UploadQuestionaryFile( { text, setResult, notifyLoading }: Props ): JSX.Element
{
    const [file, setFile] = useState<File | null>(null);

    function handleFileChange(e: ChangeEvent<HTMLInputElement>) {
        setFile(e.target.files?.[0] || null);
    };

    async function handleUpload() {
        if (!file) return;

        const text: string = await file.text();

        notifyLoading(true);

        BackendAPI.uploadQuestionaryFile(
            "UploadQuestionaryFile.handleUpload",
            text,
            (data: QuestionaryTitle) => {
                notifyLoading(false);
                setResult({
                    questionaryTitle: data.title,
                    success: true,
                });
            },
            (/* error */) => {
                notifyLoading(false);
                setResult({ success: false });
            }
        );
    };

    return (
        <div className="UploadQuestionaryFile">
            <div className="Text">
                {text}
            </div>
            <div className="Input">
                <input type="file" accept="application/json" onChange={handleFileChange} />
                <button className="UploadBtn" onClick={handleUpload}>Hochladen</button>
            </div>
        </div>
    );
};
