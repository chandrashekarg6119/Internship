import { useState } from "react";
import { useParams } from "react-router-dom";
import API from "../services/api";

function Editor() {
  const { id } = useParams(); // problem id
  const [code, setCode] = useState("");
  const [result, setResult] = useState("");

  const submitCode = async () => {
    try {
      const res = await API.post(`/submit/${id}`, code, {
        headers: {
          "Content-Type": "text/plain"
        }
      });

      setResult(res.data);
    } catch (err) {
      setResult("Error submitting code");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
    <center><h1><i>Mini Coding Platform</i></h1></center>
      <h2>💻 Code Editor (Problem {id})</h2>

      <textarea
        rows="15"
        cols="70"
        placeholder="Write your Java code here..."
        onChange={(e) => setCode(e.target.value)}
      />

      <br /><br />

      <button onClick={submitCode}>Submit Code</button>

      <h3>Result: {result}</h3>
    </div>
  );
}

export default Editor;