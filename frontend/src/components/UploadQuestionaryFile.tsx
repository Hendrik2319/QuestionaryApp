import type { ChangeEvent, JSX } from "react";
import { useState } from "react";
import { BackendAPI } from "../BackendAPI";
import type { QuestionaryTitle } from "../Types";

type Props = {
    changeTitle: (title: string) => void,
}

function UploadQuestionaryFile( { changeTitle }: Readonly<Props> ): JSX.Element {
    const [file, setFile] = useState<File | null>(null);

    function handleFileChange(e: ChangeEvent<HTMLInputElement>) {
        setFile(e.target.files?.[0] || null);
    };

    async function handleUpload() {
        if (!file) return;

        const text: string = await file.text();

        BackendAPI.uploadQuestionaryFile(
            "UploadQuestionaryFile.handleUpload",
            text,
            (data: QuestionaryTitle) => {
                alert("Upload erfolgreich");
                changeTitle( data.title );
            },
            (/* error */) => {
                alert("Upload fehlgeschlagen (details in console)");
            }
        );
    };

    return (
        <div>
            Select Questionary File:<br/>
            <input type="file" accept="application/json" onChange={handleFileChange} />
            <button onClick={handleUpload}>Upload</button>
        </div>
    );
};

export default UploadQuestionaryFile;