import React, { createContext, useContext, useState, useEffect } from 'react';
import { vendorService } from '../services/vendorService';
import { useAuth } from './AuthContext';

const VendorContext = createContext();

export const useVendor = () => {
  const context = useContext(VendorContext);
  if (!context) {
    throw new Error('useVendor must be used within VendorProvider');
  }
  return context;
};

export const VendorProvider = ({ children }) => {
  const [vendorProfile, setVendorProfile] = useState(null);
  const [vendorBooks, setVendorBooks] = useState([]);
  const [vendorOrders, setVendorOrders] = useState([]);
  const [dashboard, setDashboard] = useState(null);
  const [loading, setLoading] = useState(false);

  const { user } = useAuth();

  // Initialize vendor data when user becomes vendor - FIXED: Only depend on user properties
  useEffect(() => {
    if (user && user.role === 'VENDOR') {
      fetchVendorData();
    } else {
      clearVendorData();
    }
  }, [user?.id, user?.role]); // FIXED: Only specific user properties

  // NEW: Listen for vendor approval events
  useEffect(() => {
    const handleVendorApproved = (event) => {
      console.log('🔔 VendorContext: Received vendor approval event:', event.detail);
      // Refresh vendor profile to get updated approval status
      fetchVendorProfile();
    };

    window.addEventListener('vendorApproved', handleVendorApproved);

    return () => {
      window.removeEventListener('vendorApproved', handleVendorApproved);
    };
  }, []);

  const clearVendorData = () => {
    setVendorProfile(null);
    setVendorBooks([]);
    setVendorOrders([]);
    setDashboard(null);
  };

  const fetchVendorData = async () => {
    if (!user || user.role !== 'VENDOR') return;

    try {
      await Promise.all([
        fetchVendorProfile(),
        fetchVendorBooks(),
        fetchVendorDashboard()
      ]);
    } catch (error) {
      console.error('Error fetching vendor data:', error);
    }
  };

  const fetchVendorProfile = async () => {
    try {
      const response = await vendorService.getProfile();
      if (response.success) {
        setVendorProfile(response.data);
      }
    } catch (error) {
      console.error('Error fetching vendor profile:', error);
    }
  };

  const fetchVendorBooks = async () => {
    try {
      const response = await vendorService.getBooks();
      if (response.success) {
        setVendorBooks(response.data);
      }
    } catch (error) {
      console.error('Error fetching vendor books:', error);
    }
  };

  const fetchVendorOrders = async () => {
    try {
      const response = await vendorService.getOrders();
      if (response.success) {
        setVendorOrders(response.data);
      }
    } catch (error) {
      console.error('Error fetching vendor orders:', error);
    }
  };

  const fetchVendorDashboard = async () => {
    try {
      const response = await vendorService.getDashboard();
      if (response.success) {
        setDashboard(response.data);
      }
    } catch (error) {
      console.error('Error fetching vendor dashboard:', error);
    }
  };

  // Vendor actions
  const registerVendor = async (vendorData) => {
    setLoading(true);
    try {
      const response = await vendorService.register(vendorData);
      if (response.success) {
        await fetchVendorProfile();
      }
      return response;
    } catch (error) {
      console.error('Error registering vendor:', error);
      return { success: false, message: 'Failed to register vendor' };
    } finally {
      setLoading(false);
    }
  };

  const addBook = async (bookData) => {
    setLoading(true);
    try {
      const response = await vendorService.addBook(bookData);
      if (response.success) {
        await fetchVendorBooks();
        await fetchVendorDashboard();
      }
      return response;
    } catch (error) {
      console.error('Error adding book:', error);
      return { success: false, message: 'Failed to add book' };
    } finally {
      setLoading(false);
    }
  };

  const updateBook = async (bookId, bookData) => {
    setLoading(true);
    try {
      const response = await vendorService.updateBook(bookId, bookData);
      if (response.success) {
        await fetchVendorBooks();
      }
      return response;
    } catch (error) {
      console.error('Error updating book:', error);
      return { success: false, message: 'Failed to update book' };
    } finally {
      setLoading(false);
    }
  };

  const deleteBook = async (bookId) => {
    setLoading(true);
    try {
      const response = await vendorService.deleteBook(bookId);
      if (response.success) {
        await fetchVendorBooks();
        await fetchVendorDashboard();
      }
      return response;
    } catch (error) {
      console.error('Error deleting book:', error);
      return { success: false, message: 'Failed to delete book' };
    } finally {
      setLoading(false);
    }
  };

  const updateOrderStatus = async (orderId, status) => {
    setLoading(true);
    try {
      const response = await vendorService.updateOrderStatus(orderId, status);
      if (response.success) {
        await fetchVendorOrders();
      }
      return response;
    } catch (error) {
      console.error('Error updating order status:', error);
      return { success: false, message: 'Failed to update order status' };
    } finally {
      setLoading(false);
    }
  };

  const value = {
    // State
    vendorProfile,
    vendorBooks,
    vendorOrders,
    dashboard,
    loading,

    // Actions
    registerVendor,
    addBook,
    updateBook,
    deleteBook,
    updateOrderStatus,

    // Refresh functions
    fetchVendorProfile,
    fetchVendorBooks,
    fetchVendorOrders,
    fetchVendorDashboard,
    fetchVendorData
  };

  return (
    <VendorContext.Provider value={value}>
      {children}
    </VendorContext.Provider>
  );
};