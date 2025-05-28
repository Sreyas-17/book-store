// src/api/axiosConfig.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - adds token to all requests
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    console.log(' API Request:', config.method?.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    console.error(' Request Error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor - handles responses and errors globally
apiClient.interceptors.response.use(
  (response) => {
    console.log(' API Response:', response.status, response.config.url);
    
    // Return data in your existing format
    if (response.data && typeof response.data === 'object') {
      return response.data; // Backend returns { success: true, data: ... }
    }
    
    // Fallback for non-standard responses
    return {
      success: true,
      data: response.data,
      message: 'Request successful'
    };
  },
  (error) => {
    console.error(' API Error:', error.response?.status, error.response?.data);
    
    // Handle 401 - Unauthorized (token expired)
    if (error.response?.status === 401) {
      console.log(' Token expired, logging out...');
      localStorage.removeItem('token');
      window.location.reload();
    }
    
    // Return error in your existing format
    const errorResponse = {
      success: false,
      message: error.response?.data?.message || error.message || 'Network error occurred',
      status: error.response?.status
    };
    
    return Promise.resolve(errorResponse); // Don't reject, return error format
  }
);

export default apiClient;