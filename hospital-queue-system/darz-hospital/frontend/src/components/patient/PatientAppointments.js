import React, { useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getPatientAppointments, cancelAppointment } from '../../services/api';

const PatientAppointments = () => {
  const { user } = useAuth();
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchAppointments = async () => {
    try {
      const res = await getPatientAppointments(user.userId);
      setAppointments(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchAppointments(); }, []);

  const handleCancel = async (id) => {
    if (!window.confirm('Cancel this appointment?')) return;
    try {
      await cancelAppointment(id, user.userId);
      fetchAppointments();
    } catch (err) {
      alert('Failed to cancel appointment');
    }
  };

  if (loading) return <p>Loading appointments...</p>;

  return (
    <div>
      <h2 className="page-header">My Appointments</h2>
      {appointments.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', color: 'var(--gray-500)' }}>
          <p style={{ fontSize: 48, marginBottom: 12 }}>📭</p>
          <p>No appointments yet. Book your first appointment!</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          {appointments.map(a => (
            <div key={a.id} className="card" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div>
                <h3 style={{ marginBottom: 6 }}>Dr. {a.doctorName}</h3>
                <p style={{ color: 'var(--gray-500)', fontSize: 13, marginBottom: 4 }}>{a.doctorSpecialization}</p>
                <p style={{ fontSize: 14, marginBottom: 4 }}>📅 {a.appointmentDate} at {a.appointmentTime}</p>
                <p style={{ fontSize: 13, color: 'var(--gray-500)' }}>Queue #: <strong>{a.queueNumber}</strong></p>
                {a.symptoms && <p style={{ fontSize: 13, marginTop: 4 }}>Symptoms: {a.symptoms}</p>}
              </div>
              <div style={{ textAlign: 'right', display: 'flex', flexDirection: 'column', gap: 10, alignItems: 'flex-end' }}>
                <span className={`badge badge-${a.status?.toLowerCase()}`}>{a.status}</span>
                {(a.status === 'CONFIRMED' || a.status === 'PENDING') && (
                  <button className="btn btn-danger" style={{ fontSize: 13, padding: '6px 14px' }} onClick={() => handleCancel(a.id)}>
                    Cancel
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default PatientAppointments;
