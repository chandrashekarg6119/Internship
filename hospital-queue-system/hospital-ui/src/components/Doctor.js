import React, { useEffect, useState } from "react";
import { getDoctors } from "../api";

function Doctor() {
  const [doctors, setDoctors] = useState([]);

  useEffect(() => {
    getDoctors().then(data => setDoctors(data));
  }, []);

  return (
    <div>
      <h2>Doctors</h2>
      {doctors.map(d => (
        <p key={d.id}>{d.name} - {d.specialization}</p>
      ))}
    </div>
  );
}

export default Doctor;