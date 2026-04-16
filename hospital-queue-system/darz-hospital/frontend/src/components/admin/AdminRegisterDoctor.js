import React, { useState } from 'react';
import { registerDoctor } from '../../services/api';

const AdminRegisterDoctor = ({ onRegistered }) => {
  const [form, setForm] = useState({
    name: '', email: '', password: '', phone: '',
    specialization: '', qualification: '', experienceYears: '', consultationFee: '', bio: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true); setError('');
    try {
      await registerDoctor({ ...form, experienceYears: Number(form.experienceYears), consultationFee: Number(form.consultationFee) });
      alert('Doctor registered successfully!');
      onRegistered();
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed.');
    } finally {
      setLoading(false);
    }
  };

  const specializations = ['General Physician','Cardiologist','Dermatologist','Neurologist','Orthopedic','Pediatrician','Gynecologist','Psychiatrist','Ophthalmologist','ENT Specialist'];

  return (
    <div>
      <h2 className="page-header">Register New Doctor</h2>
      <div className="card" style={{ maxWidth: 700 }}>
        <form onSubmit={handleSubmit}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
            <div className="form-group">
              <label>Full Name *</label>
              <input name="name" value={form.name} onChange={handleChange} required placeholder="Dr. John Smith" />
            </div>
            <div className="form-group">
              <label>Email *</label>
              <input type="email" name="email" value={form.email} onChange={handleChange} required placeholder="doctor@hospital.com" />
            </div>
            <div className="form-group">
              <label>Password *</label>
              <input type="password" name="password" value={form.password} onChange={handleChange} required placeholder="Min 6 characters" minLength={6} />
            </div>
            <div className="form-group">
              <label>Phone</label>
              <input name="phone" value={form.phone} onChange={handleChange} placeholder="+91 9876543210" />
            </div>
            <div className="form-group">
              <label>Specialization *</label>
              <select name="specialization" value={form.specialization} onChange={handleChange} required>
                <option value="">Select Specialization</option>
                {specializations.map(s => <option key={s}>{s}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label>Qualification *</label>
              <input name="qualification" value={form.qualification} onChange={handleChange} required placeholder="MBBS, MD, etc." />
            </div>
            <div className="form-group">
              <label>Experience (Years) *</label>
              <input type="number" name="experienceYears" value={form.experienceYears} onChange={handleChange} required min={0} max={60} />
            </div>
            <div className="form-group">
              <label>Consultation Fee (₹) *</label>
              <input type="number" name="consultationFee" value={form.consultationFee} onChange={handleChange} required min={0} />
            </div>
          </div>
          <div className="form-group">
            <label>Bio / About</label>
            <textarea name="bio" rows={3} value={form.bio} onChange={handleChange} placeholder="Brief description about the doctor..." />
          </div>
          {error && <p className="error-msg" style={{ marginBottom: 12 }}>{error}</p>}
          <div style={{ display: 'flex', gap: 12 }}>
            <button className="btn btn-primary" type="submit" disabled={loading}>
              {loading ? 'Registering...' : 'Register Doctor'}
            </button>
            <button className="btn btn-outline" type="button" onClick={onRegistered}>Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AdminRegisterDoctor;
