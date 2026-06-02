import type { ChangeEvent, JSX } from "react";
import { useState } from "react";
import { BackendAPI } from "../BackendAPI";
import type { LoadingMsg, QuestionaryTitle } from "../types/Types";

type Props = {
    changeTitle: (title: string) => void,
    setLoading?: (isLoading: boolean) => void,
    setLoadingMsg?: (msg: LoadingMsg) => void,
}

function UploadQuestionaryFile( { changeTitle, setLoading, setLoadingMsg }: Readonly<Props> ): JSX.Element
{
    const [file, setFile] = useState<File | null>(null);

    function handleFileChange(e: ChangeEvent<HTMLInputElement>) {
        setFile(e.target.files?.[0] || null);
    };

    async function handleUpload() {
        if (!file) return;

        const text: string = await file.text();

        if (setLoading   ) setLoading   (true);
        if (setLoadingMsg) setLoadingMsg({ message: "Upload Questionary", isLoading: true });

        BackendAPI.uploadQuestionaryFile(
            "UploadQuestionaryFile.handleUpload",
            text,
            (data: QuestionaryTitle) => {
                if (setLoading   ) setLoading   (false);
                if (setLoadingMsg) setLoadingMsg({ message:"", isLoading:false })
                changeTitle( data.title );
                alert("Upload erfolgreich");
            },
            (/* error */) => {
                if (setLoading   ) setLoading   (false);
                if (setLoadingMsg) setLoadingMsg({ message:"", isLoading:false })
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