import { useEffect, useState } from "react";
import API from "../services/api";
import { useNavigate } from "react-router-dom";

function Problems() {
  const [problems, setProblems] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    API.get("/problems")
      .then(res => setProblems(res.data))
      .catch(err => console.log(err));
  }, []);

  return (
    <div>
      <h2>Problems</h2>

      {problems.map(p => (
        <div key={p.id} style={{border:"1px solid gray", padding:"10px", margin:"10px"}}>
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