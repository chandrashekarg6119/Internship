import React from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import './Navbar.css';

const Navbar = ({ title }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <span className="brand-icon">🏥</span>
        <span className="brand-name">DarZ Hospital</span>
        {title && <span className="brand-sub">| {title}</span>}
      </div>
      <div className="navbar-right">
        <span className="user-name">👤 {user?.name}</span>
        <span className={`role-badge role-${user?.role?.toLowerCase()}`}>{user?.role}</span>
        <button className="btn btn-outline" onClick={handleLogout}>Logout</button>
      </div>
    </nav>
  );
};

export default Navbar;
