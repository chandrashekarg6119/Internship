import React, { useState } from 'react';
import Navbar from '../components/common/Navbar';
import Sidebar from '../components/common/Sidebar';
import PatientAppointments from '../components/patient/PatientAppointments';
import BookAppointment from '../components/patient/BookAppointment';
import QueueTracker from '../components/patient/QueueTracker';
import PatientProfile from '../components/patient/PatientProfile';
import './Dashboard.css';

const sidebarItems = [
  { key: 'appointments', icon: '📋', label: 'My Appointments' },
  { key: 'book', icon: '📅', label: 'Book Appointment' },
  { key: 'queue', icon: '🔢', label: 'Queue Tracker' },
  { key: 'profile', icon: '👤', label: 'My Profile' },
];

const PatientDashboard = () => {
  const [active, setActive] = useState('appointments');

  const renderContent = () => {
    switch (active) {
      case 'appointments': return <PatientAppointments />;
      case 'book': return <BookAppointment onBooked={() => setActive('appointments')} />;
      case 'queue': return <QueueTracker />;
      case 'profile': return <PatientProfile />;
      default: return <PatientAppointments />;
    }
  };

  return (
    <div className="dashboard-wrapper">
      <Navbar title="Patient Dashboard" />
      <div className="dashboard-body">
        <Sidebar items={sidebarItems} active={active} onSelect={setActive} />
        <main className="dashboard-main">{renderContent()}</main>
      </div>
    </div>
  );
};

export default PatientDashboard;
