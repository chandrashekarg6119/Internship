import React, { useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getPatientProfile, updatePatient } from '../../services/api';

const PatientProfile = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [form, setForm] = useState({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [msg, setMsg] = useState('');

  useEffect(() => {
    getPatientProfile(user.userId).then(res => {
      setProfile(res.data);
      setForm({
        dateOfBirth: res.data.dateOfBirth || '',
        gender: res.data.gender || '',
        bloodGroup: res.data.bloodGroup || '',
        address: res.data.address || '',
        emergencyContact: res.data.emergencyContact || '',
        medicalHistory: res.data.medicalHistory || '',
      });
    }).catch(console.error).finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true); setMsg('');
    try {
      await updatePatient(profile.id, form);
      setMsg('Profile updated successfully!');
    } catch {
      setMsg('Update failed.');
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <p>Loading profile...</p>;

  return (
    <div>
      <h2 className="page-header">My Profile</h2>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
        <div className="card">
          <h3 style={{ marginBottom: 16, fontSize: 15, fontWeight: 600 }}>Account Info</h3>
          <p><strong>Name:</strong> {profile?.name}</p>
          <p style={{ marginTop: 10 }}><strong>Email:</strong> {profile?.email}</p>
          <p style={{ marginTop: 10 }}><strong>Phone:</strong> {profile?.phone || '—'}</p>
        </div>
        <div className="card">
          <h3 style={{ marginBottom: 16, fontSize: 15, fontWeight: 600 }}>Medical Details</h3>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Date of Birth</label>
              <input type="date" value={form.dateOfBirth} onChange={e => setForm({ ...form, dateOfBirth: e.target.value })} />
            </div>
            <div className="form-group">
              <label>Gender</label>
              <select value={form.gender} onChange={e => setForm({ ...form, gender: e.target.value })}>
                <option value="">Select</option>
                <option>Male</option><option>Female</option><option>Other</option>
              </select>
            </div>
            <div className="form-group">
              <label>Blood Group</label>
              <select value={form.bloodGroup} onChange={e => setForm({ ...form, bloodGroup: e.target.value })}>
                <option value="">Select</option>
                {['A+','A-','B+','B-','AB+','AB-','O+','O-'].map(bg => <option key={bg}>{bg}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label>Address</label>
              <textarea rows={2} value={form.address} onChange={e => setForm({ ...form, address: e.target.value })} />
            </div>
            <div className="form-group">
              <label>Emergency Contact</label>
              <input value={form.emergencyContact} onChange={e => setForm({ ...form, emergencyContact: e.target.value })} />
            </div>
            <div className="form-group">
              <label>Medical History</label>
              <textarea rows={3} value={form.medicalHistory} onChange={e => setForm({ ...form, medicalHistory: e.target.value })} />
            </div>
            {msg && <p style={{ color: msg.includes('success') ? 'var(--secondary)' : 'var(--danger)', marginBottom: 12, fontSize: 13 }}>{msg}</p>}
            <button className="btn btn-primary" type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save Changes'}</button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default PatientProfile;
