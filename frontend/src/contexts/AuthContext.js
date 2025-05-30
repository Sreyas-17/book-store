// src/contexts/AuthContext.js
import React, { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/authService';

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

  // Initialize auth on app start
  useEffect(() => {
    const initializeAuth = async () => {
      if (token) {
        console.log('Token found, fetching user profile...');
        const userData = await fetchUserProfile();
        if (!userData) {
          handleLogout();
        }
      }
      setIsInitialized(true);
    };

    initializeAuth();
  }, []);

  const fetchUserProfile = async () => {
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
      return null;
    }
  };

  const handleLogin = async (email, password) => {
    setLoading(true);
    try {
      console.log('Attempting login for:', email);
      const response = await authService.login(email, password);
      
      if (response.success && response.data) {
        const newToken = response.data;
        console.log('Login successful, setting token...');
        
        // Set token first
        setToken(newToken);
        localStorage.setItem('token', newToken);
        
        // Fetch user profile with new token
        const userResponse = await authService.getProfile();
        console.log('Profile response after login:', userResponse);
        
        if (userResponse.success && userResponse.data) {
          setUser(userResponse.data);
          console.log('User set successfully:', userResponse.data.email);
          
          // Return success with navigation flag
          return { 
            success: true, 
            shouldNavigateHome: true 
          };
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
      
      // Return structured error response
      return {
        success: false,
        message: error.response?.data?.message || error.message || 'Registration failed',
        status: error.response?.status || 500
      };
    } finally {
      setLoading(false);
    }
  };
  

  const handleLogout = () => {
    console.log('Logging out user...');
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    authService.logout();
    
    // Return logout success with navigation flag
    return { success: true, shouldNavigateLogin: true };
  };

  const value = {
    user,
    token,
    loading,
    isInitialized,
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