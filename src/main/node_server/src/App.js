import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './components/LoginPage';
import SignupPage from './components/SignUpPage';
//import Dashboard from "./components/Dashboard";
import 'mdb-react-ui-kit/dist/css/mdb.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';


function App() {
  return (
      <div className="App">
      <Router>

            <Routes>
                <Route path="/" element={<LoginPage/>} />
                <Route path="/signup" element={ <SignupPage/>} />
                {/*<Route path = "/dashboard" element={<Dashboard/>}/>*/}
            </Routes>

      </Router>
      </div>
  );
}

export default App;
