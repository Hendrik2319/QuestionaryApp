import axios from "axios";
import type { ChangeEvent } from "react";
import { useState } from "react";

function UploadQuestionaryFile() {
    const [file, setFile] = useState<File | null>(null);

    function handleFileChange(e: ChangeEvent<HTMLInputElement>) {
        setFile(e.target.files?.[0] || null);
    };

    async function handleUpload() {
        if (!file) return;

        const text = await file.text();

        axios.post(
            "/api/setquestionary",
            text,
            {
                headers: {
                    "Content-Type": "text/plain",
                },
            }
        )
            .then((/* response */) => {
                alert("Upload erfolgreich");
            })
            .catch((error) => {
                const errorData = error?.response?.data;
                const message   = errorData?.error;
                const timestamp = errorData?.timestamp;
                if (message || timestamp)
                    console.error("ERROR[setting questionary file]", error, "\r\nCause:\r\n"+ message, "\r\n\r\nTimestamp:\r\n"+timestamp);
                else
                    console.error("ERROR[setting questionary file]", error);
                alert("Upload fehlgeschlagen (details in console)");
            });
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