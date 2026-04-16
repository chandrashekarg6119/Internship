import React, { useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getDoctorAppointments, updateAppointmentStatus } from '../../services/api';

const DoctorAppointments = () => {
  const { user } = useAuth();
  const [appointments, setAppointments] = useState([]);
  const [filter, setFilter] = useState('ALL');
  const [loading, setLoading] = useState(true);

  const fetchAppointments = async () => {
    try {
      const res = await getDoctorAppointments(user.userId);
      setAppointments(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchAppointments(); }, []);

  const handleStatusChange = async (id, status) => {
    try {
      await updateAppointmentStatus(id, status);
      fetchAppointments();
    } catch {
      alert('Status update failed');
    }
  };

  const filtered = filter === 'ALL' ? appointments : appointments.filter(a => a.status === filter);

  if (loading) return <p>Loading appointments...</p>;

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <h2 className="page-header" style={{ marginBottom: 0 }}>Patient Appointments</h2>
        <div style={{ display: 'flex', gap: 8 }}>
          {['ALL','PENDING','CONFIRMED','IN_PROGRESS','COMPLETED','CANCELLED'].map(s => (
            <button
              key={s}
              onClick={() => setFilter(s)}
              className="btn"
              style={{
                fontSize: 12,
                padding: '6px 12px',
                background: filter === s ? 'var(--primary)' : 'var(--gray-200)',
                color: filter === s ? '#fff' : 'var(--gray-700)'
              }}
            >{s}</button>
          ))}
        </div>
      </div>

      {filtered.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', color: 'var(--gray-500)', padding: 48 }}>
          <p style={{ fontSize: 48, marginBottom: 12 }}>📭</p>
          <p>No appointments found.</p>
        </div>
      ) : (
        <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
          <table className="table">
            <thead>
              <tr>
                <th>Queue #</th>
                <th>Patient</th>
                <th>Date</th>
                <th>Time</th>
                <th>Symptoms</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(a => (
                <tr key={a.id}>
                  <td><strong>#{a.queueNumber}</strong></td>
                  <td>{a.patientName}</td>
                  <td>{a.appointmentDate}</td>
                  <td>{a.appointmentTime}</td>
                  <td style={{ maxWidth: 150, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {a.symptoms || '—'}
                  </td>
                  <td><span className={`badge badge-${a.status?.toLowerCase()}`}>{a.status}</span></td>
                  <td>
                    <div style={{ display: 'flex', gap: 6 }}>
                      {a.status === 'CONFIRMED' && (
                        <button className="btn btn-success" style={{ fontSize: 12, padding: '4px 10px' }}
                          onClick={() => handleStatusChange(a.id, 'IN_PROGRESS')}>Start</button>
                      )}
                      {a.status === 'IN_PROGRESS' && (
                        <button className="btn btn-primary" style={{ fontSize: 12, padding: '4px 10px' }}
                          onClick={() => handleStatusChange(a.id, 'COMPLETED')}>Complete</button>
                      )}
                      {(a.status === 'CONFIRMED' || a.status === 'PENDING') && (
                        <button className="btn btn-danger" style={{ fontSize: 12, padding: '4px 10px' }}
                          onClick={() => handleStatusChange(a.id, 'CANCELLED')}>Cancel</button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default DoctorAppointments;
