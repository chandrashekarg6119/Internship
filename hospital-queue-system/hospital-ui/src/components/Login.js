import React, { useState } from "react";
import { loginUser } from "../api";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async () => {
    const res = await loginUser({ username, password });

    if (res.token) {
      localStorage.setItem("token", res.token);
      alert("Login Successful");
      window.location.href = "/dashboard";
    } else {
      alert("Login Failed");
    }
  };

  return (
    <div>
      <h2>Login</h2>
      <input placeholder="Username" onChange={e => setUsername(e.target.value)} /><br/><br/>
      <input type="password" placeholder="Password" onChange={e => setPassword(e.target.value)} /><br/><br/>
      <button onClick={handleLogin}>Login</button>
    </div>
  );
}

export default Login;