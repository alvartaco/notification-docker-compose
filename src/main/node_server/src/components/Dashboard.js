// WelcomeDashboard.js
import React, { useEffect, useState } from 'react';
import { useNavigate} from 'react-router-dom'; // Import useHistory hook

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
            const jwtToken = localStorage.getItem('jwtToken');
            // Set the token as a cookie before redirecting
            const expirationDate = new Date();
            expirationDate.setTime(expirationDate.getTime() + (2 * 60)); // 2 minutes
            document.cookie = `jwtToken=${jwtToken}; path=/; domain=localhost; expires=${expirationDate.toUTCString()}`;
            window.location.href = 'http://localhost:8082/web';
        } catch (error) {
            console.error('Error redirecting to /web:', error);
        }
    };

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
                <h2 className="mb-4 text-center">Welcome to Notifications</h2>
                <p className="mb-4 text-center">Hello, {username}!</p>
                <p className="text-center">You are logged in successfully.</p>
                <div className="text-center">
                    <button type="button" className="btn btn-primary mt-3" onClick={handleContinue}>HTMX Notifications App.</button>
                </div>
            </div>
        </div>
    );
}

export default WelcomeDashboard;
