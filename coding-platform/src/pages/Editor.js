import { useState } from "react";
import { useParams } from "react-router-dom";
import API from "../services/api";

function Editor() {
  const { id } = useParams();
  const [code, setCode] = useState("");

  const submit = async () => {
    const res = await API.post(`/submit/${id}`, code, {
      headers: { "Content-Type": "text/plain" }
    });

    alert(res.data);
  };

  return (
    <div className="container">
      <h2>💻 Code Editor</h2>

      <textarea
        rows="15"
        cols="70"
        placeholder="Write your Java code here..."
        onChange={e => setCode(e.target.value)}
      />

      <br />
      <button onClick={submit}>Submit Code</button>
    </div>
  );
}

export default Editor;