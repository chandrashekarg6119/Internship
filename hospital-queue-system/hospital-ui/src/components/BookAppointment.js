import React, { useState } from "react";
import { bookAppointment } from "../api";

function BookAppointment() {
  const [doctorId, setDoctorId] = useState("");

  const handleBook = async () => {
    await bookAppointment({ doctorId });
    alert("Appointment Booked");
  };

  return (
    <div>
      <h2>Book Appointment</h2>
      <input placeholder="Doctor ID" onChange={e => setDoctorId(e.target.value)} />
      <button onClick={handleBook}>Book</button>
    </div>
  );
}

export default BookAppointment;