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
  const [loading, setLoading] = useState(false);
  const [isInitialized, setIsInitialized] = useState(false);

  // Initialize auth on app start - FIXED: Removed function dependencies
  useEffect(() => {
    const initializeAuth = async () => {
      try {
        console.log('Initializing auth...');

        if (authService.isLoggedIn()) {
          console.log('Found existing login, loading user data...');
          const userData = authService.getCurrentUser();

          if (userData) {
            console.log('User data loaded from localStorage:', userData);
            setUser(userData);
          } else {
            console.log('No user data in localStorage, trying to fetch profile...');
            const profileResult = await authService.getProfile();
            if (profileResult.success) {
              console.log('Profile fetched from API:', profileResult.data);
              setUser(profileResult.data);
            } else {
              console.log('Failed to fetch profile, clearing auth...');
              authService.logout();
            }
          }
        } else {
          console.log('No existing login found');
        }
      } catch (error) {
        console.error('Auth initialization error:', error);
        authService.logout();
      } finally {
        setIsInitialized(true);
        console.log('Auth initialization complete');
      }
    };

    initializeAuth();
  }, []); // FIXED: Empty dependency array

  const handleLogin = async (email, password) => {
    setLoading(true);
    try {
      console.log('AuthContext: Attempting login for:', email);
      const response = await authService.login(email, password);
      console.log('AuthContext: Login service response:', response);

      if (response.success && response.user) {
        console.log('AuthContext: Login successful, user data:', response.user);
        setUser(response.user);

        return {
          success: true,
          user: response.user,
          shouldNavigateHome: true
        };
      } else {
        console.log('AuthContext: Login failed:', response.message);
        return {
          success: false,
          message: response.message || 'Login failed. Please check your credentials.'
        };
      }
    } catch (error) {
      console.error('AuthContext: Login error:', error);
      return {
        success: false,
        message: 'Network error occurred. Please try again.'
      };
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (userData, role = 'USER') => {
    setLoading(true);
    try {
      console.log('AuthContext: Attempting registration for:', userData.email, 'as', role);

      let response;
      switch (role) {
        case 'VENDOR':
          response = await authService.registerVendor(userData);
          break;
        case 'ADMIN':
          response = await authService.registerAdmin(userData);
          break;
        default:
          response = await authService.register(userData);
      }

      console.log('AuthContext: Register response:', response);
      return response;
    } catch (error) {
      console.error('AuthContext: Register error:', error);
      return { success: false, message: 'Registration failed. Please try again.' };
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    console.log('AuthContext: Logging out user...');
    setUser(null);
    authService.logout();
    return { success: true, shouldNavigateLogin: true };
  };

  const updateUser = (userData) => {
    console.log('AuthContext: Updating user data:', userData);
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
  };

  // Helper functions - FIXED: Use user state directly
  const isAuthenticated = () => {
    return !!user && authService.isLoggedIn();
  };

  const hasRole = (role) => {
    return user && user.role === role;
  };

  const isAdmin = () => hasRole('ADMIN');
  const isVendor = () => hasRole('VENDOR');
  const isUser = () => hasRole('USER');

  // Get user display name
  const getUserDisplayName = () => {
    if (!user) return 'Guest';
    if (user.role === 'VENDOR' && user.businessName) {
      return user.businessName;
    }
    return `${user.firstName} ${user.lastName}`;
  };

  const value = {
    // State
    user,
    loading,
    isInitialized,

    // Actions
    handleLogin,
    handleRegister,
    handleLogout,
    updateUser,

    // Cleaner names
    login: handleLogin,
    register: handleRegister,
    logout: handleLogout,

    // Helpers
    isAuthenticated,
    hasRole,
    isAdmin,
    isVendor,
    isUser,
    getUserDisplayName
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};