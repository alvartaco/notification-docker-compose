// WelcomeDashboard.js
import React, { useEffect, useState } from 'react';
import { useNavigate} from 'react-router-dom'; // Import useHistory hook
import axiosInstance from '../api/axiosInstance';

function WelcomeDashboard() {
    const history = useNavigate();
    const [username, setUsername] = useState('');

    const handleLogout = () => {
        // Perform logout actions here (e.g., clear session, remove authentication token)
        // After logout, redirect to the login page
        history('/');
    };

    const handleContinue = async () => {
        try {
            // Make a request to /web using axiosInstance
            //await axiosInstance.get('http://localhost:8082/auth/validate');
            fetchDataWithJwt('http://localhost:8082/auth/validate');
        } catch (error) {
            console.error('Error redirecting to /auth/validate:', error);
            // Handle the error (e.g., show a message to the user)
        }
    };

    // Example using fetch API for an HTMX request
    async function fetchDataWithJwt(url) {
        const jwtToken = localStorage.getItem('jwtToken');
        const response = await fetch(url, {
            headers: {
                'Authorization': `Bearer ${jwtToken}` // Send as a Bearer token
            }
        });
    }

    useEffect(() => {
        // Retrieve user data from local storage
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
            const user = JSON.parse(storedUser);
            // Assuming your API returns a 'fullName' field
            setUsername(user.user.fullName);
        }

    }, []);

    return (
        <div className="d-flex justify-content-center align-items-center vh-100">
            <div className="border rounded-lg p-4" style={{width: '500px', height: '400px'}}>
                <h2 className="mb-4 text-center">Welcome to Dashboard</h2>
                <p className="mb-4 text-center">Hello, {username}!</p>
                <p className="text-center">You are logged in successfully.</p>
                <div className="text-center">
                    <button type="button" className="btn btn-primary mt-3" onClick={handleContinue}>Continue to Notifications App</button>
                </div>
                <div className="text-center">
                    <button type="button" className="btn btn-primary mt-3" onClick={handleLogout}>Logout</button>
                </div>
            </div>
        </div>
    );
}

export default WelcomeDashboard;
