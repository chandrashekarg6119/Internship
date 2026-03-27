import { useState } from "react";
import API from "../services/api";
import { useNavigate } from "react-router-dom";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const login = async () => {
    try {
      const res = await API.post("/auth/login", {
        username,
        password
      });

      alert(res.data);

      if (res.data === "Login successful") {
        navigate("/problems");
      }
    } catch (error) {
      alert("Error connecting to backend");
    }
  };

  return (

    <div>
    <center><h1><i>Mini Coding Platform</i></h1></center>
      <h2>Login</h2>

      <input
        placeholder="Username"
        onChange={e => setUsername(e.target.value)}
      />

      <br />

      <input
        type="password"
        placeholder="Password"
        onChange={e => setPassword(e.target.value)}
      />

      <br />

      <button onClick={login}>Login</button>
    </div>
  );
}

export default Login;