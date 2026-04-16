import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { login as loginApi } from '../services/api';
import './Auth.css';

const Login = () => {
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const quickLogin = (email, password) => setForm({ email, password });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await loginApi(form);
      login(res.data);
      const role = res.data.role;
      if (role === 'PATIENT') navigate('/patient');
      else if (role === 'DOCTOR') navigate('/doctor');
      else if (role === 'ADMIN') navigate('/admin');
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid credentials. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-bg">
      <div className="auth-card">
        <div className="auth-logo">🏥</div>
        <h1 className="auth-title">DarZ Hospital</h1>
        <p className="auth-subtitle">Sign in to your account</p>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email Address</label>
            <input
              type="email"
              name="email"
              placeholder="you@example.com"
              value={form.email}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              placeholder="••••••••"
              value={form.password}
              onChange={handleChange}
              required
            />
          </div>
          {error && <p className="error-msg">{error}</p>}
          <button className="btn btn-primary auth-btn" type="submit" disabled={loading}>
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>

        <p className="auth-link">
          Don't have an account? <Link to="/register">Register as Patient</Link>
        </p>

        <div className="demo-credentials">
          <p style={{ fontWeight: 700, marginBottom: 8 }}>🔑 Quick Login (click to fill)</p>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            <button
              type="button"
              onClick={() => quickLogin('admin@darz.com', 'admin123')}
              style={{ background: '#fce4ec', border: '1px solid #f48fb1', borderRadius: 6, padding: '6px 10px', cursor: 'pointer', textAlign: 'left', fontSize: 12 }}>
              👑 <strong>Admin</strong> — admin@darz.com / admin123
            </button>
            <button
              type="button"
              onClick={() => quickLogin('doctor1@darz.com', 'doctor123')}
              style={{ background: '#e8f5e9', border: '1px solid #a5d6a7', borderRadius: 6, padding: '6px 10px', cursor: 'pointer', textAlign: 'left', fontSize: 12 }}>
              👨‍⚕️ <strong>Doctor</strong> — doctor1@darz.com / doctor123
            </button>
            <button
              type="button"
              onClick={() => quickLogin('patient1@darz.com', 'patient123')}
              style={{ background: '#e3f2fd', border: '1px solid #90caf9', borderRadius: 6, padding: '6px 10px', cursor: 'pointer', textAlign: 'left', fontSize: 12 }}>
              🧑 <strong>Patient</strong> — patient1@darz.com / patient123
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
