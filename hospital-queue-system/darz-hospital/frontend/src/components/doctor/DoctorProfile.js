import React, { useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getDoctorProfile } from '../../services/api';

const DoctorProfile = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    getDoctorProfile(user.userId).then(res => setProfile(res.data)).catch(console.error);
  }, []);

  if (!profile) return <p>Loading profile...</p>;

  return (
    <div>
      <h2 className="page-header">My Profile</h2>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
        <div className="card">
          <div style={{ textAlign: 'center', marginBottom: 20 }}>
            <div style={{ width: 80, height: 80, borderRadius: '50%', background: 'var(--primary)', color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 32, margin: '0 auto 12px' }}>
              👨‍⚕️
            </div>
            <h3>{profile.name}</h3>
            <p style={{ color: 'var(--primary)', fontWeight: 600 }}>{profile.specialization}</p>
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12, fontSize: 14 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ color: 'var(--gray-500)' }}>Email</span><span>{profile.email}</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ color: 'var(--gray-500)' }}>Phone</span><span>{profile.phone || '—'}</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ color: 'var(--gray-500)' }}>Qualification</span><span>{profile.qualification}</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ color: 'var(--gray-500)' }}>Experience</span><span>{profile.experienceYears} years</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ color: 'var(--gray-500)' }}>Consultation Fee</span>
              <span style={{ color: 'var(--secondary)', fontWeight: 600 }}>₹{profile.consultationFee}</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ color: 'var(--gray-500)' }}>Status</span>
              <span style={{ color: profile.isAvailable ? 'var(--secondary)' : 'var(--danger)', fontWeight: 600 }}>
                {profile.isAvailable ? '✅ Available' : '❌ Unavailable'}
              </span>
            </div>
          </div>
        </div>
        <div className="card">
          <h3 style={{ marginBottom: 16, fontSize: 15, fontWeight: 600 }}>About</h3>
          <p style={{ color: 'var(--gray-500)', fontSize: 14, lineHeight: 1.8 }}>{profile.bio || 'No bio available.'}</p>
          <h3 style={{ marginTop: 20, marginBottom: 12, fontSize: 15, fontWeight: 600 }}>Working Hours</h3>
          <p style={{ fontSize: 14 }}>🕐 {profile.availableTimeStart || '—'} — {profile.availableTimeEnd || '—'}</p>
        </div>
      </div>
    </div>
  );
};

export default DoctorProfile;
