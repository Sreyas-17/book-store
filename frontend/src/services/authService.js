import { authApi } from '../api/authApi';

export const authService = {
  async login(email, password) {
    const response = await authApi.login({ email, password });
    if (response.success && response.data) {
      localStorage.setItem('token', response.data);
    }
    return response;
  },

  async register(userData) {
    return await authApi.register(userData);
  },

  async getProfile() {
    return await authApi.getProfile();
  },

  logout() {
    localStorage.removeItem('token');
    return { success: true };
  }
};
