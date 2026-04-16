import React, { useEffect, useState } from 'react';
import { getAllDoctors, deleteDoctor } from '../../services/api';

const AdminDoctors = () => {
  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchDoctors = async () => {
    try {
      const res = await getAllDoctors();
      setDoctors(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchDoctors(); }, []);

  const handleDelete = async (id, name) => {
    if (!window.confirm(`Remove Dr. ${name}?`)) return;
    try {
      await deleteDoctor(id);
      fetchDoctors();
    } catch {
      alert('Failed to delete doctor');
    }
  };

  if (loading) return <p>Loading...</p>;

  return (
    <div>
      <h2 className="page-header">All Doctors ({doctors.length})</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 20 }}>
        {doctors.map(doc => (
          <div key={doc.id} className="card">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 12 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                <div style={{ width: 48, height: 48, borderRadius: '50%', background: 'var(--primary)', color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 20 }}>👨‍⚕️</div>
                <div>
                  <h4>Dr. {doc.name}</h4>
                  <p style={{ color: 'var(--primary)', fontSize: 13 }}>{doc.specialization}</p>
                </div>
              </div>
              <span style={{ fontSize: 12, color: doc.isAvailable ? 'var(--secondary)' : 'var(--danger)', fontWeight: 600 }}>
                {doc.isAvailable ? '● Available' : '● Unavailable'}
              </span>
            </div>
            <div style={{ fontSize: 13, color: 'var(--gray-500)', display: 'flex', flexDirection: 'column', gap: 4, marginBottom: 12 }}>
              <span>📧 {doc.email}</span>
              <span>🎓 {doc.qualification} • {doc.experienceYears} yrs</span>
              <span>💰 ₹{doc.consultationFee}</span>
            </div>
            <button className="btn btn-danger" style={{ fontSize: 12, padding: '6px 14px' }} onClick={() => handleDelete(doc.id, doc.name)}>
              Remove Doctor
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AdminDoctors;
