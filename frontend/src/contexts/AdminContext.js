import React, { createContext, useContext, useState, useEffect } from 'react';
import { adminService } from '../services/adminService';
import { useAuth } from './AuthContext';

const AdminContext = createContext();

export const useAdmin = () => {
  const context = useContext(AdminContext);
  if (!context) {
    throw new Error('useAdmin must be used within AdminProvider');
  }
  return context;
};

export const AdminProvider = ({ children }) => {
  const [dashboard, setDashboard] = useState(null);
  const [pendingBooks, setPendingBooks] = useState([]);
  const [pendingVendors, setPendingVendors] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [allOrders, setAllOrders] = useState([]);
  const [loading, setLoading] = useState(false);

  const { user } = useAuth();

  // Initialize admin data when user becomes admin - FIXED: Only depend on user properties
  useEffect(() => {
    if (user && user.role === 'ADMIN') {
      fetchAdminData();
    } else {
      clearAdminData();
    }
  }, [user?.id, user?.role]); // FIXED: Only specific user properties

  const clearAdminData = () => {
    setDashboard(null);
    setPendingBooks([]);
    setPendingVendors([]);
    setAllUsers([]);
    setAllOrders([]);
  };

  const fetchAdminData = async () => {
    if (!user || user.role !== 'ADMIN') return;

    try {
      await Promise.all([
        fetchDashboard(),
        fetchPendingBooks(),
        fetchPendingVendors()
      ]);
    } catch (error) {
      console.error('Error fetching admin data:', error);
    }
  };

  const fetchDashboard = async () => {
    try {
      const response = await adminService.getDashboard();
      if (response.success) {
        setDashboard(response.data);
      }
    } catch (error) {
      console.error('Error fetching admin dashboard:', error);
    }
  };

  const fetchPendingBooks = async () => {
    try {
      const response = await adminService.getPendingBooks();
      if (response.success) {
        setPendingBooks(response.data);
      }
    } catch (error) {
      console.error('Error fetching pending books:', error);
    }
  };

  const fetchPendingVendors = async () => {
    try {
      const response = await adminService.getPendingVendors();
      if (response.success) {
        setPendingVendors(response.data);
      }
    } catch (error) {
      console.error('Error fetching pending vendors:', error);
    }
  };

  const fetchAllUsers = async () => {
    try {
      const response = await adminService.getAllUsers();
      if (response.success) {
        setAllUsers(response.data);
      }
    } catch (error) {
      console.error('Error fetching all users:', error);
    }
  };

  const fetchAllOrders = async () => {
    try {
      const response = await adminService.getAllOrders();
      if (response.success) {
        setAllOrders(response.data);
      }
    } catch (error) {
      console.error('Error fetching all orders:', error);
    }
  };

  // Admin actions
  const approveBook = async (bookId) => {
    setLoading(true);
    try {
      const response = await adminService.approveBook(bookId);
      if (response.success) {
        await fetchPendingBooks();
        await fetchDashboard();
      }
      return response;
    } catch (error) {
      console.error('Error approving book:', error);
      return { success: false, message: 'Failed to approve book' };
    } finally {
      setLoading(false);
    }
  };

  const disapproveBook = async (bookId) => {
    setLoading(true);
    try {
      const response = await adminService.disapproveBook(bookId);
      if (response.success) {
        await fetchPendingBooks();
        await fetchDashboard();
      }
      return response;
    } catch (error) {
      console.error('Error disapproving book:', error);
      return { success: false, message: 'Failed to disapprove book' };
    } finally {
      setLoading(false);
    }
  };

  const approveVendor = async (vendorId) => {
    setLoading(true);
    try {
      console.log('🔄 AdminContext: Approving vendor with ID:', vendorId);
      const response = await adminService.approveVendor(vendorId);

      if (response.success) {
        console.log('✅ AdminContext: Vendor approved successfully');
        await fetchPendingVendors();
        await fetchDashboard();

        // NEW: Force refresh of vendor data in other contexts
        window.dispatchEvent(new CustomEvent('vendorApproved', {
          detail: { vendorId }
        }));

        return response;
      } else {
        console.log('❌ AdminContext: Vendor approval failed:', response.message);
        return response;
      }
    } catch (error) {
      console.error('💥 AdminContext: Error approving vendor:', error);
      return { success: false, message: 'Failed to approve vendor' };
    } finally {
      setLoading(false);
    }
  };

  const disapproveVendor = async (vendorId) => {
    setLoading(true);
    try {
      const response = await adminService.disapproveVendor(vendorId);
      if (response.success) {
        await fetchPendingVendors();
        await fetchDashboard();
      }
      return response;
    } catch (error) {
      console.error('Error disapproving vendor:', error);
      return { success: false, message: 'Failed to disapprove vendor' };
    } finally {
      setLoading(false);
    }
  };

  const updateUserRole = async (userId, role) => {
    setLoading(true);
    try {
      const response = await adminService.updateUserRole(userId, role);
      if (response.success) {
        await fetchAllUsers();
      }
      return response;
    } catch (error) {
      console.error('Error updating user role:', error);
      return { success: false, message: 'Failed to update user role' };
    } finally {
      setLoading(false);
    }
  };

  const value = {
    // State
    dashboard,
    pendingBooks,
    pendingVendors,
    allUsers,
    allOrders,
    loading,

    // Actions
    approveBook,
    disapproveBook,
    approveVendor,
    disapproveVendor,
    updateUserRole,

    // Fetch functions
    fetchDashboard,
    fetchPendingBooks,
    fetchPendingVendors,
    fetchAllUsers,
    fetchAllOrders,
    fetchAdminData
  };

  return (
    <AdminContext.Provider value={value}>
      {children}
    </AdminContext.Provider>
  );
};