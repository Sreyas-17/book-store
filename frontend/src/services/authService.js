// src/services/authService.js - Enhanced version
import { authApi } from '../api/authApi';

export const authService = {
  async login(email, password) {
    try {
      const response = await authApi.login({ email, password });
      console.log('Raw API response:', response);

      if (response.success && response.data) {
        // Backend returns LoginResponse object with token and user details
        const loginData = response.data;

        // Store token
        if (loginData.token) {
          localStorage.setItem('token', loginData.token);
        }

        // Store user details with proper mapping
        const userDetails = {
          id: loginData.userId, // Map userId to id for consistency
          userId: loginData.userId,
          email: loginData.email,
          firstName: loginData.firstName,
          lastName: loginData.lastName,
          role: loginData.role,
          isVerified: loginData.isVerified
        };

        // Store vendor details if user is a vendor
        if (loginData.role === 'VENDOR' && loginData.vendorId) {
          userDetails.vendorId = loginData.vendorId;
          userDetails.businessName = loginData.businessName;
          userDetails.vendorApproved = loginData.vendorApproved;
        }

        localStorage.setItem('user', JSON.stringify(userDetails));

        console.log('Stored user details:', userDetails);

        return {
          success: true,
          data: loginData,
          user: userDetails
        };
      }

      return response;
    } catch (error) {
      console.error('Login service error:', error);
      return {
        success: false,
        message: 'Login failed. Please try again.'
      };
    }
  },

  async register(userData) {
    try {
      return await authApi.register(userData);
    } catch (error) {
      console.error('Register service error:', error);
      return {
        success: false,
        message: 'Registration failed. Please try again.'
      };
    }
  },

  // NEW: Register as vendor
  async registerVendor(userData) {
    try {
      return await authApi.registerVendor(userData);
    } catch (error) {
      console.error('Vendor register service error:', error);
      return {
        success: false,
        message: 'Vendor registration failed. Please try again.'
      };
    }
  },

  // NEW: Register as admin
  async registerAdmin(userData) {
    try {
      return await authApi.registerAdmin(userData);
    } catch (error) {
      console.error('Admin register service error:', error);
      return {
        success: false,
        message: 'Admin registration failed. Please try again.'
      };
    }
  },

  async getProfile() {
    try {
      // First check if we have user data in localStorage
      const storedUser = localStorage.getItem('user');
      if (storedUser) {
        return {
          success: true,
          data: JSON.parse(storedUser)
        };
      }

      // If not, fetch from API
      const response = await authApi.getProfile();
      if (response.success && response.data) {
        // Ensure the user has an id field for consistency
        const userData = {
          ...response.data,
          id: response.data.id || response.data.userId
        };
        localStorage.setItem('user', JSON.stringify(userData));
        return {
          success: true,
          data: userData
        };
      }
      return response;
    } catch (error) {
      console.error('Get profile service error:', error);
      return {
        success: false,
        message: 'Failed to load user details'
      };
    }
  },

  getCurrentUser() {
    try {
      const storedUser = localStorage.getItem('user');
      const user = storedUser ? JSON.parse(storedUser) : null;
      // Ensure id field exists for consistency
      if (user && !user.id && user.userId) {
        user.id = user.userId;
      }
      return user;
    } catch (error) {
      console.error('Error getting current user:', error);
      return null;
    }
  },

  getToken() {
    return localStorage.getItem('token');
  },

  isLoggedIn() {
    return !!this.getToken() && !!this.getCurrentUser();
  },

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    return { success: true };
  },

  // NEW: Forgot password
  async forgotPassword(email) {
    try {
      return await authApi.forgotPassword(email);
    } catch (error) {
      console.error('Forgot password service error:', error);
      return {
        success: false,
        message: 'Failed to process password reset request.'
      };
    }
  },

  async updateProfile(userId, profileData) {
    try {
      return await authApi.updateProfile(userId, profileData);
    } catch (error) {
      console.error('Update profile service error:', error);
      return {
        success: false,
        message: 'Failed to update profile.'
      };
    }
  }
};