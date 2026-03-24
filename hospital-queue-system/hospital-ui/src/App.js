import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

import Login from "./components/Login";
import Register from "./components/Register";
import Dashboard from "./components/Dashboard";
import Doctor from "./components/Doctor";
import BookAppointment from "./components/BookAppointment";
import Appointments from "./components/Appointments";

function App() {
  return (

    <Router>
      <div style={{ padding: "20px" }}>
        <h1>Hospital Queue System</h1>

        <nav>
          <Link to="/">Login</Link> |{" "}
          <Link to="/register">Register</Link> |{" "}
          <Link to="/dashboard">Dashboard</Link> |{" "}
          <Link to="/doctors">Doctors</Link> |{" "}
          <Link to="/book">Book</Link> |{" "}
          <Link to="/appointments">Appointments</Link>
        </nav>

        <hr />

        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/doctor" element={<Doctor />} />
          <Route path="/book" element={<BookAppointment />} />
          <Route path="/appointments" element={<Appointments />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;