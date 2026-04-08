import React from "react";

function Dashboard() {
  return (
    <div>
      <h2>Dashboard</h2>
      <h3>Wellcome to Hospital</h3>
      <p>A hospital is a specialized healthcare institution providing 24/7 inpatient and outpatient medical care,
       diagnostics, and treatment for sick or injured individuals. Staffed by skilled professionals,
        these facilities include emergency services, surgical theaters, and ICUs.
         Modern hospitals often serve as centers for research, education,
          and community health.</p>
          <h4> In This Website </h4>
          <p>You Can View Doctors</p>
          <p>You Can Book Your Appointment</p>
          <p>You Can View Your Schedule</p>
      <button onClick={() => {
        localStorage.removeItem("token");
        window.location.href = "/";
      }}>
        Logout
      </button>
    </div>
  );
}

export default Dashboard;