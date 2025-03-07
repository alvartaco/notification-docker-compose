import React, { useState } from 'react';
//import axios from 'axios';
import axiosInstance from '../api/axiosInstance';
import { useNavigate } from 'react-router-dom';
import {
    MDBContainer,
    MDBInput,
    MDBBtn,
} from 'mdb-react-ui-kit';

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const history = useNavigate();

    // Email validation function using regex
    const validateEmail = (email) => {
        const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        return emailRegex.test(email);
    };

    const handleLogin = async () => {
            // Reset error message
            setError('');

            // Basic field check
            if (!email || !password) {
                setError('Please enter both email and password.');
                return;
            }

            // Email validation
            if (!validateEmail(email)) {
                setError('Please enter a valid email address.');
                return;
            }

            try {
                const response = await axiosInstance.post('http://localhost:8082/auth/signin', { email, password });
                console.log('Login successful:', response.data);
                // Store user data in local storage
                localStorage.setItem('user', JSON.stringify(response.data));

                localStorage.setItem('jwtToken', response.data.jwt);
                console.log('jwtToken:', response.data.jwt);

                history('/dashboard');
            } catch (error) {
                console.error('Login failed:', error.response ? error.response.data : error.message);
                setError('Invalid email or password.');
            }
        };

        const handleEmailChange = (e) => {
            setEmail(e.target.value);
            if(error==='Please enter a valid email address.'){
                setError('');
            }
        };
        const handlePasswordChange = (e) => {
            setPassword(e.target.value);
            if(error==='Please enter both email and password.'){
                setError('');
            }
        };


    return (
            <div className="d-flex justify-content-center align-items-center vh-100">
                <div className="border rounded-lg p-4" style={{ width: '500px', height: 'auto' }}>
                    <MDBContainer className="p-3">
                        <h2 className="mb-4 text-center">Notifications Login</h2>
                        <MDBInput
                            wrapperClass='mb-4'
                            label='Email address'
                            id='email'
                            value={email}
                            type='email'
                            onChange={handleEmailChange} // Call handleEmailChange here
                        />
                        <MDBInput
                            wrapperClass='mb-4'
                            label='Password'
                            id='password'
                            type='password'
                            value={password}
                            onChange={handlePasswordChange} // Call handlePasswordChange here
                        />
                        {error && <p className="text-danger">{error}</p>}
                        <MDBBtn
                            className="mb-4 d-block btn-primary"
                            style={{ height: '50px', width: '100%' }}
                            onClick={handleLogin}
                        >
                            Sign in
                        </MDBBtn>
                        <div className="text-center">
                            <p>Not a member? <a href="/signup" >Register</a></p>
                        </div>
                    </MDBContainer>
                </div>
            </div>
        );
}

export default LoginPage;
