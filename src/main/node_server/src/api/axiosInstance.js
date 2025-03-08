import axios from 'axios';
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8082', // Your API base URL
});
// Add a request interceptor
axiosInstance.interceptors.request.use(
    (config) => {
        // Get the token from localStorage
        const token = localStorage.getItem('jwtToken');
        // If there's a token, add the Authorization header
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        // Do something with request error
        return Promise.reject(error);
    }
);
export default axiosInstance;