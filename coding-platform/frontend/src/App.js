import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Problems from "./pages/Problems";
import Editor from "./pages/Editor";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/problems" element={<Problems />} />
        <Route path="/editor/:id" element={<Editor />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;