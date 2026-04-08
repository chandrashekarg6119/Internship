const BASE_URL = "http://localhost:8080";

export const registerUser = (data) =>
  fetch(`${BASE_URL}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  }).then(res => res.json());

export const loginUser = (data) =>
  fetch(`${BASE_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  }).then(res => res.json());

export const getDoctors = () =>
  fetch(`${BASE_URL}/doctors`).then(res => res.json());

export const bookAppointment = (data) =>
  fetch(`${BASE_URL}/appointments/book`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  }).then(res => res.json());

export const getAppointments = () =>
  fetch(`${BASE_URL}/appointments`).then(res => res.json());