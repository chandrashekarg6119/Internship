import React, { useEffect, useState } from 'react';
import { getAllAppointments, updateAppointmentStatus } from '../../services/api';

const AdminAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [filter, setFilter] = useState('ALL');
  const [loading, setLoading] = useState(true);

  const fetchAppointments = async () => {
    try {
      const res = await getAllAppointments();
      setAppointments(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchAppointments(); }, []);

  const handleStatus = async (id, status) => {
    try {
      await updateAppointmentStatus(id, status);
      fetchAppointments();
    } catch {
      alert('Failed to update status');
    }
  };

  const filtered = filter === 'ALL' ? appointments : appointments.filter(a => a.status === filter);

  if (loading) return <p>Loading...</p>;

  const stats = {
    total: appointments.length,
    confirmed: appointments.filter(a => a.status === 'CONFIRMED').length,
    completed: appointments.filter(a => a.status === 'COMPLETED').length,
    cancelled: appointments.filter(a => a.status === 'CANCELLED').length,
  };

  return (
    <div>
      <h2 className="page-header">All Appointments</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 16, marginBottom: 24 }}>
        {[
          { label: 'Total', value: stats.total, color: 'var(--primary)' },
          { label: 'Confirmed', value: stats.confirmed, color: 'var(--secondary)' },
          { label: 'Completed', value: stats.completed, color: '#6c757d' },
          { label: 'Cancelled', value: stats.cancelled, color: 'var(--danger)' },
        ].map(s => (
          <div key={s.label} className="card" style={{ textAlign: 'center', padding: 16 }}>
            <p style={{ fontSize: 32, fontWeight: 800, color: s.color }}>{s.value}</p>
            <p style={{ fontSize: 13, color: 'var(--gray-500)' }}>{s.label}</p>
          </div>
        ))}
      </div>

      <div style={{ display: 'flex', gap: 8, marginBottom: 16 }}>
        {['ALL','PENDING','CONFIRMED','IN_PROGRESS','COMPLETED','CANCELLED'].map(s => (
          <button key={s} onClick={() => setFilter(s)} className="btn"
            style={{ fontSize: 12, padding: '6px 12px', background: filter === s ? 'var(--primary)' : 'var(--gray-200)', color: filter === s ? '#fff' : 'var(--gray-700)' }}>
            {s}
          </button>
        ))}
      </div>

      <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
        <table className="table">
          <thead>
            <tr><th>#</th><th>Patient</th><th>Doctor</th><th>Date</th><th>Time</th><th>Status</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {filtered.map(a => (
              <tr key={a.id}>
                <td>#{a.queueNumber}</td>
                <td>{a.patientName}</td>
                <td>Dr. {a.doctorName}<br /><span style={{ fontSize: 12, color: 'var(--gray-500)' }}>{a.doctorSpecialization}</span></td>
                <td>{a.appointmentDate}</td>
                <td>{a.appointmentTime}</td>
                <td><span className={`badge badge-${a.status?.toLowerCase()}`}>{a.status}</span></td>
                <td>
                  <div style={{ display: 'flex', gap: 6 }}>
                    {a.status === 'PENDING' && <button className="btn btn-success" style={{ fontSize: 11, padding: '3px 8px' }} onClick={() => handleStatus(a.id, 'CONFIRMED')}>Confirm</button>}
                    {a.status === 'CONFIRMED' && <button className="btn btn-primary" style={{ fontSize: 11, padding: '3px 8px' }} onClick={() => handleStatus(a.id, 'IN_PROGRESS')}>Start</button>}
                    {a.status === 'IN_PROGRESS' && <button className="btn btn-success" style={{ fontSize: 11, padding: '3px 8px' }} onClick={() => handleStatus(a.id, 'COMPLETED')}>Complete</button>}
                    {!['CANCELLED','COMPLETED'].includes(a.status) && <button className="btn btn-danger" style={{ fontSize: 11, padding: '3px 8px' }} onClick={() => handleStatus(a.id, 'CANCELLED')}>Cancel</button>}
                  </div>
                </td>
              </tr>
            ))}
            {filtered.length === 0 && <tr><td colSpan={7} style={{ textAlign: 'center', color: 'var(--gray-500)', padding: 32 }}>No appointments found.</td></tr>}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default AdminAppointments;
