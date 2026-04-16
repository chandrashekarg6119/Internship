import React, { useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getDoctorProfile, updateAvailability } from '../../services/api';

const DoctorAvailability = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [form, setForm] = useState({ availableDates: [], availableTimeStart: '', availableTimeEnd: '', isAvailable: true });
  const [newDate, setNewDate] = useState('');
  const [saving, setSaving] = useState(false);
  const [msg, setMsg] = useState('');

  useEffect(() => {
    getDoctorProfile(user.userId).then(res => {
      const d = res.data;
      setProfile(d);
      setForm({
        availableDates: d.availableDates || [],
        availableTimeStart: d.availableTimeStart || '',
        availableTimeEnd: d.availableTimeEnd || '',
        isAvailable: d.isAvailable !== false,
      });
    }).catch(console.error);
  }, []);

  const addDate = () => {
    if (!newDate || form.availableDates.includes(newDate)) return;
    setForm({ ...form, availableDates: [...form.availableDates, newDate].sort() });
    setNewDate('');
  };

  const removeDate = (date) => {
    setForm({ ...form, availableDates: form.availableDates.filter(d => d !== date) });
  };

  const handleSave = async () => {
    setSaving(true); setMsg('');
    try {
      await updateAvailability(profile.id, form);
      setMsg('Availability updated successfully!');
    } catch {
      setMsg('Update failed.');
    } finally {
      setSaving(false);
    }
  };

  if (!profile) return <p>Loading...</p>;

  return (
    <div>
      <h2 className="page-header">Manage Availability</h2>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
        <div className="card">
          <h3 style={{ marginBottom: 16, fontSize: 15, fontWeight: 600 }}>Consultation Hours</h3>
          <div className="form-group">
            <label>Status</label>
            <div style={{ display: 'flex', gap: 12, marginTop: 4 }}>
              {[true, false].map(v => (
                <label key={String(v)} style={{ display: 'flex', alignItems: 'center', gap: 6, cursor: 'pointer', marginBottom: 0 }}>
                  <input type="radio" checked={form.isAvailable === v} onChange={() => setForm({ ...form, isAvailable: v })} />
                  {v ? '✅ Available' : '❌ Unavailable'}
                </label>
              ))}
            </div>
          </div>
          <div className="form-group">
            <label>Start Time</label>
            <input type="time" value={form.availableTimeStart}
              onChange={e => setForm({ ...form, availableTimeStart: e.target.value })} />
          </div>
          <div className="form-group">
            <label>End Time</label>
            <input type="time" value={form.availableTimeEnd}
              onChange={e => setForm({ ...form, availableTimeEnd: e.target.value })} />
          </div>
          {msg && <p style={{ color: msg.includes('success') ? 'var(--secondary)' : 'var(--danger)', marginBottom: 12, fontSize: 13 }}>{msg}</p>}
          <button className="btn btn-primary" onClick={handleSave} disabled={saving}>
            {saving ? 'Saving...' : 'Save Changes'}
          </button>
        </div>

        <div className="card">
          <h3 style={{ marginBottom: 16, fontSize: 15, fontWeight: 600 }}>Available Dates</h3>
          <div style={{ display: 'flex', gap: 8, marginBottom: 16 }}>
            <input type="date" value={newDate} min={new Date().toISOString().split('T')[0]}
              onChange={e => setNewDate(e.target.value)} style={{ flex: 1 }} />
            <button className="btn btn-primary" onClick={addDate}>Add</button>
          </div>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
            {form.availableDates.length === 0 && <p style={{ color: 'var(--gray-500)', fontSize: 13 }}>No dates added yet.</p>}
            {form.availableDates.map(date => (
              <div key={date} style={{
                display: 'flex', alignItems: 'center', gap: 6,
                background: 'var(--gray-100)', padding: '6px 12px', borderRadius: 20, fontSize: 13
              }}>
                📅 {date}
                <button onClick={() => removeDate(date)} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--danger)', fontSize: 16, lineHeight: 1 }}>×</button>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DoctorAvailability;
