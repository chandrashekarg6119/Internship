import React, { useEffect, useState } from "react";
import { getAppointments } from "../api";

function Appointments() {
  const [appointments, setAppointments] = useState([]);

  useEffect(() => {
    getAppointments().then(data => setAppointments(data));
  }, []);

  return (
    <div>
      <h2>Appointments</h2>
      {appointments.map(a => (
        <p key={a.id}>
          Doctor: {a.doctorId} | Status: {a.status}
        </p>
      ))}
    </div>
  );
}

export default Appointments;