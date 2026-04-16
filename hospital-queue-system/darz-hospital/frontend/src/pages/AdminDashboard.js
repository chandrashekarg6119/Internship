import React, { useState } from 'react';
import Navbar from '../components/common/Navbar';
import Sidebar from '../components/common/Sidebar';
import AdminDoctors from '../components/admin/AdminDoctors';
import AdminPatients from '../components/admin/AdminPatients';
import AdminAppointments from '../components/admin/AdminAppointments';
import AdminRegisterDoctor from '../components/admin/AdminRegisterDoctor';
import './Dashboard.css';

const sidebarItems = [
  { key: 'doctors', icon: '👨‍⚕️', label: 'Doctors' },
  { key: 'patients', icon: '🧑‍🤝‍🧑', label: 'Patients' },
  { key: 'appointments', icon: '📋', label: 'Appointments' },
  { key: 'register-doctor', icon: '➕', label: 'Register Doctor' },
];

const AdminDashboard = () => {
  const [active, setActive] = useState('doctors');

  const renderContent = () => {
    switch (active) {
      case 'doctors': return <AdminDoctors />;
      case 'patients': return <AdminPatients />;
      case 'appointments': return <AdminAppointments />;
      case 'register-doctor': return <AdminRegisterDoctor onRegistered={() => setActive('doctors')} />;
      default: return <AdminDoctors />;
    }
  };

  return (
    <div className="dashboard-wrapper">
      <Navbar title="Admin Dashboard" />
      <div className="dashboard-body">
        <Sidebar items={sidebarItems} active={active} onSelect={setActive} />
        <main className="dashboard-main">{renderContent()}</main>
      </div>
    </div>
  );
};

export default AdminDashboard;
