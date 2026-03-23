import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Problems from "./pages/Problems";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/problems" element={<Problems />} />
        <h1>TEST WORKING</h1>
      </Routes>
    </BrowserRouter>
  );
}

export default App;