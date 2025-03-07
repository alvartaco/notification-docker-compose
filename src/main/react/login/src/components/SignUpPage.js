import React, { useState } from 'react';
//import axios from 'axios';
import axiosInstance from '../api/axiosInstance';
import { useNavigate } from 'react-router-dom'; // Import useHistory hook
import {
    MDBContainer,
    MDBInput,
    MDBBtn,
} from 'mdb-react-ui-kit';

function SignupPage() {
    const [fullName, setFullName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [role, setRole] = useState('ROLE_ADMIN');
    const [mobile, setMobileNumber] = useState('');
    const [error, setError] = useState(''); // State to manage error messages
    const history = useNavigate(); // Get the history object for redirection

    // Email validation function using regex
    const validateEmail = (email) => {
        const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        return emailRegex.test(email);
    };

    const handleSignup = async () => {
        // Reset error message
        setError('');

        // Email validation
        if (!validateEmail(email)) {
            setError('Please enter a valid email address.');
            return;
        }

        try {
            // Check for empty fields
            if (!fullName || !email || !password || !confirmPassword || !mobile) {
                setError('Please fill in all fields.');
                return;
            }

            if (password !== confirmPassword) {
                throw new Error("Passwords do not match");
            }

            const response = await axiosInstance.post('http://localhost:8082/auth/signup', {
                fullName,
                email,
                password,
                role,
                mobile
            });
            // Handle successful signup
            console.log(response.data);
            // Store user data in local storage
            localStorage.setItem('user', JSON.stringify(response.data));

            localStorage.setItem('jwtToken', response.data.jwt);
            console.log('jwtToken:', response.data.jwt);

            history('/dashboard');
        } catch (error) {
            // Handle signup error
            console.error('Signup failed:', error.response ? error.response.data : error.message);
            setError(error.response ? error.response.data : error.message);
        }
    };

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
        if(error==='Please enter a valid email address.'){
            setError('');
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center vh-100">
            <div className="border rounded-lg p-4" style={{width: '600px', height: 'auto'}}>
                <MDBContainer className="p-3">
                    <h2 className="mb-4 text-center">Notifications Sign Up</h2>
                    {/* Render error message if exists */}
                    {error && <p className="text-danger">{error}</p>}
                    <MDBInput wrapperClass='mb-3' id='fullName' label={"Full Name"} value={fullName} type='text'
                              onChange={(e) => setFullName(e.target.value)}/>
                    <MDBInput wrapperClass='mb-3' label='Email Address' id='email' value={email} type='email'
                              onChange={handleEmailChange}/>
                    <MDBInput wrapperClass='mb-3' label='Password' id='password' type='password' value={password}
                              onChange={(e) => setPassword(e.target.value)}/>
                    <MDBInput wrapperClass='mb-3' label='Confirm Password' id='confirmPassword' type='password'
                              value={confirmPassword}
                              onChange={(e) => setConfirmPassword(e.target.value)}/>


                    <MDBInput wrapperClass='mb-2' label='Mobile Number' id='mobileNumber' value={mobile}
                              type='text'
                              onChange={(e) => setMobileNumber(e.target.value)}/>
                    <label className="form-label mb-1">Role:</label>
                    <select className="form-select mb-4" value={role} onChange={(e) => setRole(e.target.value)}>
                        <option value="ROLE_ADMIN">Admin</option>
                    </select>
                    <MDBBtn className="mb-4 d-block btn-primary"
                            style={{height: '40px', width: '100%'}}
                            onClick={handleSignup}>Sign Up
                    </MDBBtn>

                    <div className="text-center">
                        <p>Already Register? <a href="/">Login</a></p>
                    </div>

                </MDBContainer>
            </div>
        </div>
    );
}

export default SignupPage;
