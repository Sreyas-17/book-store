// src/hooks/useAddresses.js
import { useState, useEffect } from 'react';
import { addressService } from '../services/addressService';
import { useAuth } from '../contexts/AuthContext';

export const useAddresses = () => {
  const [addresses, setAddresses] = useState([]);
  const { user } = useAuth();

  useEffect(() => {
    if (user?.id) {
      fetchAddresses();
    } else {
      setAddresses([]);
    }
  }, [user?.id]);

  const fetchAddresses = async (userId = user?.id) => {
    if (!userId) return;
    
    try {
      console.log('Fetching addresses for user:', userId);
      const response = await addressService.getUserAddresses(userId);
      if (response.success) {
        setAddresses(response.data || []); // Ensure it's always an array
        console.log('Addresses fetched:', response.data?.length || 0, 'items');
      }
    } catch (error) {
      console.error('Error fetching addresses:', error);
      setAddresses([]); // Set empty array on error
    }
  };

  const addAddress = async (addressData) => {
    if (!user?.id) {
      return { success: false, message: 'Please login first' };
    }

    try {
      const dataWithUserId = {
        ...addressData,
        userId: user.id
      };
      
      const response = await addressService.addAddress(dataWithUserId);
      
      if (response.success) {
        await fetchAddresses(); // Refresh addresses
        return response;
      }
      
      return response;
    } catch (error) {
      console.error('Error adding address:', error);
      return { success: false, message: 'Failed to add address' };
    }
  };

  return {
    addresses,
    fetchAddresses,
    addAddress
  };
};