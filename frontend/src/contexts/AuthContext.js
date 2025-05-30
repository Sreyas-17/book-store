import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { authService } from '../services/authService'; // Keep this as is

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [loading, setLoading] = useState(false);
  const [isInitialized, setIsInitialized] = useState(false);

  const fetchUserProfile = useCallback(async () => {
    if (!token) return null;
    
    try {
      const response = await authService.getProfile();
      if (response.success && response.data) {
        setUser(response.data);
        console.log('User profile fetched:', response.data.email);
        return response.data;
      }
      return null;
    } catch (error) {
      console.error('Error fetching user profile:', error);
      handleLogout();
      return null;
    }
  }, [token]);

  // Initialize auth on app start
  useEffect(() => {
    const initializeAuth = async () => {
      if (token) {
        console.log('Token found, fetching user profile...');
        await fetchUserProfile();
      }
      setIsInitialized(true);
    };

    initializeAuth();
  }, [token, fetchUserProfile]);

  const handleLogin = async (email, password) => {
    setLoading(true);
    try {
      console.log('Attempting login for:', email);
      const response = await authService.login(email, password);
      
      if (response.success && response.data) {
        const newToken = response.data;
        console.log('Login successful, setting token...');
        
        setToken(newToken);
        localStorage.setItem('token', newToken);
        
        // Fetch user profile with new token
        const userResponse = await authService.getProfile();
        console.log('Profile response after login:', userResponse);
        
        if (userResponse.success && userResponse.data) {
          setUser(userResponse.data);
          console.log('User set successfully:', userResponse.data.email);
          return { success: true };
        } else {
          console.error('Failed to fetch user profile after login');
          return { success: false, message: 'Failed to load user profile' };
        }
      } else {
        console.log('Login failed:', response.message);
        return response;
      }
    } catch (error) {
      console.error('Login error:', error);
      return { success: false, message: 'Network error occurred' };
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (userData) => {
    setLoading(true);
    try {
      const response = await authService.register(userData);
      console.log('Register response:', response);
      return response;
    } catch (error) {
      console.error('Registration error:', error);
      return {
        success: false,
        message: error.response?.data?.message || error.message || 'Registration failed',
        status: error.response?.status || 500
      };
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = useCallback(() => {
    console.log('Logging out user...');
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    authService.logout();
  }, []);

  const value = {
    user,
    token,
    loading,
    isInitialized,
    isAuthenticated: !!user && !!token,
    handleLogin,
    handleRegister,
    handleLogout,
    fetchUserProfile
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
