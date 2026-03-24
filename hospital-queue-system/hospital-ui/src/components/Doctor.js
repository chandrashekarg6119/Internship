import React, { useEffect, useState } from "react";
import { getDoctors } from "../api";

function Doctors() {
  const [doctors, setDoctors] = useState([]);

  useEffect(() => {
    getDoctors().then(setDoctors);
  }, []);

  return (
    <div>
      <h2>Doctors</h2>

      {doctors.map(d => (
        <div key={d.id}>
          {d.name} - {d.specialization}
        </div>
      ))}
    </div>
  );
}

export default Doctors;