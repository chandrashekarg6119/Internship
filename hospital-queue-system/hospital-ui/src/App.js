import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

import Login from "./components/Login";
import Register from "./components/Register";
import Dashboard from "./components/Dashboard";
import Doctor from "./components/Doctor";
import BookAppointment from "./components/BookAppointment";
import Appointments from "./components/Appointments";
import Profile from "./components/Profile";

import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <Router>
      <div style={{ padding: "20px" }}>
        <h1>Hospital Queue System</h1>

        <nav>
          <Link to="/">Login</Link> |{" "}
          <Link to="/register">Register</Link> |{" "}
          <Link to="/dashboard">Dashboard</Link> |{" "}
          <Link to="/doctor">Doctors</Link> |{" "}
          <Link to="/book">Book</Link> |{" "}
          <Link to="/appointments">Appointments</Link> |{" "}
          <Link to="/profile">Profile</Link>
        </nav>

        <hr />

        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route path="/dashboard" element={
            <ProtectedRoute><Dashboard /></ProtectedRoute>
          } />

          <Route path="/doctor" element={
            <ProtectedRoute><Doctor /></ProtectedRoute>
          } />

          <Route path="/book" element={
            <ProtectedRoute><BookAppointment /></ProtectedRoute>
          } />

          <Route path="/appointments" element={
            <ProtectedRoute><Appointments /></ProtectedRoute>
          } />

          <Route path="/profile" element={
            <ProtectedRoute><Profile /></ProtectedRoute>
          } />
        </Routes>
      </div>
    </Router>
  );
}

export default App;