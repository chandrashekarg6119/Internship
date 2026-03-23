import { useState } from "react";

function Login() {
  const [username, setUsername] = useState("");

  return (
    <div>
      <h1>Login Page</h1>

      <input
        placeholder="Username"
        onChange={e => setUsername(e.target.value)}
      />

      <button>Login</button>
    </div>
  );
}

export default Login;