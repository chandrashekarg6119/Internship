import React, { useState } from "react";
import { registerUser } from "../api";

function Register() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleRegister = async () => {
    await registerUser({ username, password });
    alert("Registered Successfully");
  };

  return (
    <div>
      <h2>Register</h2>
      <input placeholder="Username" onChange={e => setUsername(e.target.value)} /><br/><br/>
      <input type="password" placeholder="Password" onChange={e => setPassword(e.target.value)} /><br/><br/>
      <button onClick={handleRegister}>Register</button>
    </div>
  );
}

export default Register;