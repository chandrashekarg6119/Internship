import React, { useState } from "react";
import { bookAppointment } from "../api";

function BookAppointment() {
  const [time, setTime] = useState("");

  const handleBook = async () => {
    await bookAppointment(1, 1, { appointmentTime: time });
    alert("Booked ✅");
  };

  return (
    <div>
      <h2>Book Appointment</h2>

      <input
        type="datetime-local"
        onChange={(e) => setTime(e.target.value)}
      /><br /><br />

      <button onClick={handleBook}>Book</button>
    </div>
  );
}

export default BookAppointment;