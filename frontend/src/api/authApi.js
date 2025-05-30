import apiClient from './axiosConfig';

export const authApi = {
  login: (credentials) => apiClient.post('/auth/login', credentials),
  register: (userData) => apiClient.post('/auth/register', userData),
  registerVendor: (userData) => apiClient.post('/auth/register-vendor', userData),
  registerAdmin: (userData) => apiClient.post('/auth/register-admin', userData),
  getProfile: () => apiClient.get('/auth/profile'),
  logout: () => apiClient.post('/auth/logout')
};

export default authApi;
