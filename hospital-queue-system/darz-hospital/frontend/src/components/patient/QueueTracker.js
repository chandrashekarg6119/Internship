import React, { useEffect, useState, useRef } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getPatientAppointments, getDoctorQueue } from '../../services/api';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const QueueTracker = () => {
  const { user } = useAuth();
  const [todayAppointment, setTodayAppointment] = useState(null);
  const [queue, setQueue] = useState([]);
  const [loading, setLoading] = useState(true);
  const stompRef = useRef(null);

  const today = new Date().toISOString().split('T')[0];

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await getPatientAppointments(user.userId);
        const todayAppt = res.data.find(a => a.appointmentDate === today && a.status !== 'CANCELLED');
        setTodayAppointment(todayAppt);

        if (todayAppt) {
          const queueRes = await getDoctorQueue(todayAppt.doctorId, today);
          setQueue(queueRes.data);
          connectWebSocket(todayAppt.doctorId);
        }
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
    return () => { if (stompRef.current) stompRef.current.deactivate(); };
  }, []);

  const connectWebSocket = (doctorId) => {
    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      onConnect: () => {
        client.subscribe(`/topic/queue/${doctorId}`, (msg) => {
          setQueue(JSON.parse(msg.body));
        });
      },
    });
    client.activate();
    stompRef.current = client;
  };

  if (loading) return <p>Loading queue...</p>;

  if (!todayAppointment) {
    return (
      <div>
        <h2 className="page-header">Live Queue Tracker</h2>
        <div className="card" style={{ textAlign: 'center', color: 'var(--gray-500)', padding: 48 }}>
          <p style={{ fontSize: 48, marginBottom: 12 }}>📭</p>
          <p>No appointment scheduled for today.</p>
        </div>
      </div>
    );
  }

  const myPosition = queue.findIndex(a => a.id === todayAppointment.id);
  const ahead = myPosition;

  return (
    <div>
      <h2 className="page-header">Live Queue Tracker</h2>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24, marginBottom: 24 }}>
        <div className="card" style={{ textAlign: 'center' }}>
          <p style={{ color: 'var(--gray-500)', marginBottom: 8 }}>Your Queue Number</p>
          <p style={{ fontSize: 64, fontWeight: 800, color: 'var(--primary)' }}>#{todayAppointment.queueNumber}</p>
          <p style={{ color: 'var(--gray-500)', marginTop: 8 }}>Dr. {todayAppointment.doctorName}</p>
        </div>
        <div className="card" style={{ textAlign: 'center' }}>
          <p style={{ color: 'var(--gray-500)', marginBottom: 8 }}>Patients Ahead</p>
          <p style={{ fontSize: 64, fontWeight: 800, color: ahead > 0 ? 'var(--warning)' : 'var(--secondary)' }}>
            {ahead > 0 ? ahead : '🟢'}
          </p>
          <p style={{ color: 'var(--gray-500)', marginTop: 8 }}>
            {ahead === 0 ? "It's your turn!" : `${ahead} patient(s) ahead`}
          </p>
        </div>
      </div>

      <div className="card">
        <h3 style={{ marginBottom: 16, fontSize: 15, fontWeight: 600 }}>Queue Status (Live)</h3>
        {queue.length === 0 ? <p style={{ color: 'var(--gray-500)' }}>Queue is empty.</p> : (
          <table className="table">
            <thead>
              <tr>
                <th>#</th>
                <th>Patient</th>
                <th>Time</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {queue.map((appt) => (
                <tr key={appt.id} style={{ background: appt.id === todayAppointment.id ? '#e8f0fe' : '' }}>
                  <td><strong>{appt.queueNumber}</strong></td>
                  <td>{appt.id === todayAppointment.id ? '👤 You' : `Patient #${appt.patientId}`}</td>
                  <td>{appt.appointmentTime}</td>
                  <td><span className={`badge badge-${appt.status?.toLowerCase()}`}>{appt.status}</span></td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default QueueTracker;
