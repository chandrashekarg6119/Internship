import React, { useState } from 'react';
import Navbar from '../components/common/Navbar';
import Sidebar from '../components/common/Sidebar';
import DoctorAppointments from '../components/doctor/DoctorAppointments';
import DoctorAvailability from '../components/doctor/DoctorAvailability';
import DoctorProfile from '../components/doctor/DoctorProfile';
import './Dashboard.css';

const sidebarItems = [
  { key: 'appointments', icon: '📋', label: 'Appointments' },
  { key: 'availability', icon: '🗓️', label: 'My Availability' },
  { key: 'profile', icon: '👤', label: 'My Profile' },
];

const DoctorDashboard = () => {
  const [active, setActive] = useState('appointments');

  const renderContent = () => {
    switch (active) {
      case 'appointments': return <DoctorAppointments />;
      case 'availability': return <DoctorAvailability />;
      case 'profile': return <DoctorProfile />;
      default: return <DoctorAppointments />;
    }
  };

  return (
    <div className="dashboard-wrapper">
      <Navbar title="Doctor Dashboard" />
      <div className="dashboard-body">
        <Sidebar items={sidebarItems} active={active} onSelect={setActive} />
        <main className="dashboard-main">{renderContent()}</main>
      </div>
    </div>
  );
};

export default DoctorDashboard;
