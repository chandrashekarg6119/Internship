import React, { useEffect, useState } from "react";
import { getAppointments } from "../api";

function Appointments() {
  const [data, setData] = useState([]);

  useEffect(() => {
    getAppointments().then(setData);
  }, []);

  return (
    <div>
      <h2>Appointments</h2>

      {data.map(a => (
        <div key={a.id}>
          Queue: {a.queueNumber} | Status: {a.status}
        </div>
      ))}
    </div>
  );
}

export default Appointments;