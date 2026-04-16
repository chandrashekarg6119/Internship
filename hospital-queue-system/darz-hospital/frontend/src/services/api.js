import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('user');
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(err);
  }
);

// Auth
export const login = (data) => api.post('/auth/login', data);
export const register = (data) => api.post('/auth/register', data);

// Doctors
export const getAllDoctors = () => api.get('/doctors/public/all');
export const getAvailableDoctors = () => api.get('/doctors/public/available');
export const getDoctorById = (id) => api.get(`/doctors/public/${id}`);
export const getDoctorProfile = (userId) => api.get(`/doctor/profile/${userId}`);
export const updateAvailability = (doctorId, data) => api.put(`/doctor/${doctorId}/availability`, data);
export const registerDoctor = (data) => api.post('/admin/doctors/register', data);
export const deleteDoctor = (id) => api.delete(`/admin/doctors/${id}`);

// Patients
export const getAllPatients = () => api.get('/admin/patients');
export const getPatientProfile = (userId) => api.get(`/patient/profile/${userId}`);
export const updatePatient = (patientId, data) => api.put(`/patient/${patientId}`, data);
export const deletePatient = (id) => api.delete(`/admin/patients/${id}`);

// Appointments
export const bookAppointment = (userId, data) => api.post(`/appointments/book/${userId}`, data);
export const cancelAppointment = (appointmentId, userId) => api.put(`/appointments/${appointmentId}/cancel/${userId}`);
export const getPatientAppointments = (userId) => api.get(`/appointments/patient/${userId}`);
export const getDoctorAppointments = (userId) => api.get(`/appointments/doctor/${userId}`);
export const getDoctorQueue = (doctorId, date) => api.get(`/appointments/queue/${doctorId}?date=${date}`);
export const getAllAppointments = () => api.get('/appointments/admin/all');
export const updateAppointmentStatus = (appointmentId, status) => api.put(`/appointments/${appointmentId}/status?status=${status}`);

export default api;
