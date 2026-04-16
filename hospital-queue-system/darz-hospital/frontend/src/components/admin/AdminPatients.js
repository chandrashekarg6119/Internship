import React, { useEffect, useState } from 'react';
import { getAllPatients, deletePatient } from '../../services/api';

const AdminPatients = () => {
  const [patients, setPatients] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);

  const fetchPatients = async () => {
    try {
      const res = await getAllPatients();
      setPatients(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchPatients(); }, []);

  const handleDelete = async (id, name) => {
    if (!window.confirm(`Remove patient ${name}?`)) return;
    try {
      await deletePatient(id);
      fetchPatients();
    } catch {
      alert('Failed to delete patient');
    }
  };

  const filtered = patients.filter(p =>
    p.name.toLowerCase().includes(search.toLowerCase()) ||
    p.email.toLowerCase().includes(search.toLowerCase())
  );

  if (loading) return <p>Loading...</p>;

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <h2 className="page-header" style={{ marginBottom: 0 }}>All Patients ({patients.length})</h2>
        <input placeholder="Search by name or email..." value={search} onChange={e => setSearch(e.target.value)}
          style={{ width: 260 }} />
      </div>
      <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
        <table className="table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Blood Group</th>
              <th>Gender</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map(p => (
              <tr key={p.id}>
                <td><strong>{p.name}</strong></td>
                <td>{p.email}</td>
                <td>{p.phone || '—'}</td>
                <td>{p.bloodGroup || '—'}</td>
                <td>{p.gender || '—'}</td>
                <td>
                  <button className="btn btn-danger" style={{ fontSize: 12, padding: '4px 10px' }}
                    onClick={() => handleDelete(p.id, p.name)}>Remove</button>
                </td>
              </tr>
            ))}
            {filtered.length === 0 && (
              <tr><td colSpan={6} style={{ textAlign: 'center', color: 'var(--gray-500)', padding: 32 }}>No patients found.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default AdminPatients;
