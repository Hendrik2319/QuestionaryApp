import axios from "axios";
import { useState } from "react";
import type { ChangeEvent } from "react";

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
            .then((response) => {
                if (response.status !== 200)
                    throw new Error("Get wrong response status, when setting questionary file: " + response.status);
                alert("Upload erfolgreich");
            })
            .catch((error) => {
                console.error("ERROR[setting questionary file]", error);
                alert("Upload fehlgeschlagen");
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