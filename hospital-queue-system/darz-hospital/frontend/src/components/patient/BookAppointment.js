import React, { useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getAvailableDoctors, bookAppointment } from '../../services/api';

const BookAppointment = ({ onBooked }) => {
  const { user } = useAuth();
  const [doctors, setDoctors] = useState([]);
  const [selected, setSelected] = useState(null);
  const [form, setForm] = useState({ appointmentDate: '', appointmentTime: '', symptoms: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    getAvailableDoctors().then(res => setDoctors(res.data)).catch(console.error);
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selected) { setError('Please select a doctor'); return; }
    setLoading(true); setError('');
    try {
      await bookAppointment(user.userId, {
        doctorId: selected.id,
        appointmentDate: form.appointmentDate,
        appointmentTime: form.appointmentTime + ':00',
        symptoms: form.symptoms,
      });
      alert('Appointment booked successfully!');
      onBooked();
    } catch (err) {
      setError(err.response?.data?.message || 'Booking failed. Try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2 className="page-header">Book an Appointment</h2>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
        {/* Doctor List */}
        <div>
          <h3 style={{ marginBottom: 16, fontSize: 16, fontWeight: 600 }}>Select a Doctor</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
            {doctors.map(doc => (
              <div
                key={doc.id}
                className="card"
                style={{
                  cursor: 'pointer',
                  border: selected?.id === doc.id ? '2px solid var(--primary)' : '2px solid transparent',
                  padding: 16
                }}
                onClick={() => setSelected(doc)}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                  <div>
                    <h4>Dr. {doc.name}</h4>
                    <p style={{ color: 'var(--primary)', fontSize: 13, marginTop: 2 }}>{doc.specialization}</p>
                    <p style={{ fontSize: 12, color: 'var(--gray-500)', marginTop: 4 }}>{doc.qualification} • {doc.experienceYears} yrs exp</p>
                  </div>
                  <div style={{ textAlign: 'right' }}>
                    <p style={{ fontWeight: 700, color: 'var(--secondary)' }}>₹{doc.consultationFee}</p>
                    <p style={{ fontSize: 12, color: 'var(--gray-500)' }}>{doc.availableTimeStart} - {doc.availableTimeEnd}</p>
                  </div>
                </div>
                {doc.bio && <p style={{ fontSize: 12, color: 'var(--gray-500)', marginTop: 8, borderTop: '1px solid var(--gray-200)', paddingTop: 8 }}>{doc.bio}</p>}
              </div>
            ))}
            {doctors.length === 0 && <p style={{ color: 'var(--gray-500)' }}>No doctors available.</p>}
          </div>
        </div>

        {/* Booking Form */}
        <div className="card" style={{ height: 'fit-content' }}>
          <h3 style={{ marginBottom: 20, fontSize: 16, fontWeight: 600 }}>
            {selected ? `Book with Dr. ${selected.name}` : 'Appointment Details'}
          </h3>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Appointment Date</label>
              <input
                type="date"
                value={form.appointmentDate}
                min={new Date().toISOString().split('T')[0]}
                onChange={e => setForm({ ...form, appointmentDate: e.target.value })}
                required
              />
            </div>
            <div className="form-group">
              <label>Preferred Time</label>
              <input
                type="time"
                value={form.appointmentTime}
                onChange={e => setForm({ ...form, appointmentTime: e.target.value })}
                required
              />
            </div>
            <div className="form-group">
              <label>Symptoms / Reason (Optional)</label>
              <textarea
                rows={4}
                placeholder="Describe your symptoms..."
                value={form.symptoms}
                onChange={e => setForm({ ...form, symptoms: e.target.value })}
              />
            </div>
            {error && <p className="error-msg">{error}</p>}
            <button className="btn btn-primary" type="submit" disabled={loading} style={{ width: '100%' }}>
              {loading ? 'Booking...' : 'Confirm Booking'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default BookAppointment;
