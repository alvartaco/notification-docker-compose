import React, { useState } from 'react';
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

    // Email validation function using regex
    const validateEmail = (email) => {
        const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        return emailRegex.test(email);
    };

    const handleSignup = async (credentials) => {
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

           // 1. Get the CSRF token
           const csrfResponse = await fetch('http://localhost:8082/auth/csrf',
                {credentials: 'include'});
           if (!csrfResponse.ok) {
             throw new Error('Failed to get CSRF token');
           }

            // Extract the CSRF token from the cookie
            let csrfToken = csrfResponse.headers.get('X-CSRF-TOKEN');
            if (!csrfToken) {
                throw new Error('CSRF token not found in headers');
            }

            const response = await fetch('http://localhost:8082/auth/signup', {
            method: 'POST',
                  headers: {
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': csrfToken, // Include the CSRF token as a header
                  },
                  body: JSON.stringify(credentials),
                  credentials: 'include',// This is crucial for sending cookies

                fullName,
                email,
                password,
                role,
                mobile
            });

            if (response.ok) {
              const data = await response.json();
              localStorage.setItem('jwtToken', data.jwt);

              try {
                  // Set the token as a cookie before redirecting
                  const expirationDate = new Date();
                  expirationDate.setTime(expirationDate.getTime() + (2 * 60)); // 2 minutes
                  document.cookie = `jwtToken=${data.jwt}; path=/; domain=localhost; expires=${expirationDate.toUTCString()}`;
                  window.location.href = 'http://localhost:8082/web';
              } catch (error) {
                  console.error('Error redirecting to /web:', error);
              }
            } else {
              throw new Error('signup failed');
            }

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
                            onClick={() => handleSignup({email,password, mobile, fullName})}>Sign Up
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
