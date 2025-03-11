import React, { useState } from 'react';
import {
    MDBContainer,
    MDBInput,
    MDBBtn,
} from 'mdb-react-ui-kit';

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    // Email validation function using regex
    const validateEmail = (email) => {
        const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        return emailRegex.test(email);
    };

    const handleLogin = async (credentials) => {
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

               // 2. Send the login request with the CSRF token
                const response = await fetch('http://localhost:8082/auth/signin', {
                  method: 'POST',
                  headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrfToken, // Include the CSRF token as a header
                  },
                  body: JSON.stringify(credentials),
                  credentials: 'include',// This is crucial for sending cookies
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
                  throw new Error('Login failed');
                }
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
                            onClick={() => handleLogin({email,password})}>
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
