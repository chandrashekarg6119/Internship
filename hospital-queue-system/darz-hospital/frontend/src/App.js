import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import PatientDashboard from './pages/PatientDashboard';
import DoctorDashboard from './pages/DoctorDashboard';
import AdminDashboard from './pages/AdminDashboard';

const PrivateRoute = ({ children, role }) => {
  const { user, loading } = useAuth();
  if (loading) return <div style={{display:'flex',justifyContent:'center',alignItems:'center',height:'100vh'}}>Loading...</div>;
  if (!user) return <Navigate to="/login" />;
  if (role && user.role !== role) return <Navigate to="/" />;
  return children;
};

const HomeRedirect = () => {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" />;
  if (user.role === 'PATIENT') return <Navigate to="/patient" />;
  if (user.role === 'DOCTOR') return <Navigate to="/doctor" />;
  if (user.role === 'ADMIN') return <Navigate to="/admin" />;
  return <Navigate to="/login" />;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<HomeRedirect />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/patient/*" element={
            <PrivateRoute role="PATIENT">
              <PatientDashboard />
            </PrivateRoute>
          } />
          <Route path="/doctor/*" element={
            <PrivateRoute role="DOCTOR">
              <DoctorDashboard />
            </PrivateRoute>
          } />
          <Route path="/admin/*" element={
            <PrivateRoute role="ADMIN">
              <AdminDashboard />
            </PrivateRoute>
          } />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
