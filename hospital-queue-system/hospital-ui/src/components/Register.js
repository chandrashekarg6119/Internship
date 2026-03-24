import React, { useState } from "react";
import { registerUser } from "../api";

function Register() {
  const [form, setForm] = useState({
    username: "",
    password: "",
    email: "",
    role: "PATIENT"
  });

  const handleRegister = async () => {
    await registerUser(form);
    alert("Registered Successfully ✅");
  };

  return (
    <div>
      <h2>Register</h2>

      <input placeholder="Username" onChange={(e) => setForm({...form, username: e.target.value})}/><br /><br />
      <input placeholder="Password" onChange={(e) => setForm({...form, password: e.target.value})}/><br /><br />
      <input placeholder="Email" onChange={(e) => setForm({...form, email: e.target.value})}/><br /><br />

      <button onClick={handleRegister}>Register</button>
    </div>
  );
}

export default Register;