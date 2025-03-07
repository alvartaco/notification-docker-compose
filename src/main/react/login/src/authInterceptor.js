import axios from 'axios';

const authInterceptor = axios.create({
  baseURL: 'http://localhost:8082/', // Your base URL
});

// Add a request interceptor
authInterceptor.interceptors.request.use(
  (config) => {
    // Retrieve the token from local storage
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const user = JSON.parse(storedUser);
      //Assuming the token is returned as "jwt"
      const token = user.jwt;
      // If the token exists, add it to the Authorization header
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => {
    // Do something with request error
    return Promise.reject(error);
  }
);

export default authInterceptor;