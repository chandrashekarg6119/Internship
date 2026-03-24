const BASE_URL = "http://localhost:8080";

export const registerUser = async (data) => {
  const res = await fetch(`${BASE_URL}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  return res.json();
};

export const loginUser = async (data) => {
  const res = await fetch(`${BASE_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  return res.json();
};

export const getDoctors = async () => {
  const res = await fetch(`${BASE_URL}/doctors`);
  return res.json();
};

export const getAppointments = async () => {
  const res = await fetch(`${BASE_URL}/appointments`);
  return res.json();
};

export const bookAppointment = async (data) => {
  const res = await fetch(`${BASE_URL}/appointments/book`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  return res.json();
};