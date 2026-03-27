import { useEffect, useState } from "react";
import API from "../services/api";
import { useNavigate } from "react-router-dom";

function Problems() {
  const [problems, setProblems] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    API.get("/problems").then(res => setProblems(res.data));
  }, []);

  return (
    <div>
    <center><h1><i>Mini Coding Platform</i></h1></center>
      <h2>📝 Problems</h2>

      {problems.map(p => (
        <div key={p.id}>
          <h3>{p.title}</h3>
          <p>{p.description}</p>

          <button onClick={() => navigate(`/editor/${p.id}`)}>
            Solve
          </button>
        </div>
      ))}
    </div>
  );
}

export default Problems;