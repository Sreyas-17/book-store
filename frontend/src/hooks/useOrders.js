// src/hooks/useOrders.js
import { useState, useEffect } from 'react';
import { orderService } from '../services/orderService';
import { useAuth } from '../contexts/AuthContext';
import { useCart } from '../contexts/CartContext';

export const useOrders = () => {
  const [orders, setOrders] = useState([]);
  const { user } = useAuth();
  const { fetchCart } = useCart();

  useEffect(() => {
    if (user?.id) {
      fetchOrders();
    } else {
      setOrders([]);
    }
  }, [user?.id]);

  const fetchOrders = async (userId = user?.id) => {
    if (!userId) return;
    
    try {
      console.log('Fetching orders for user:', userId);
      const response = await orderService.getUserOrders(userId);
      if (response.success) {
        setOrders(response.data || []); // Ensure it's always an array
        console.log('Orders fetched:', response.data?.length || 0, 'items');
      }
    } catch (error) {
      console.error('Error fetching orders:', error);
      setOrders([]); // Set empty array on error
    }
  };

  const createOrder = async (addressId) => {
    if (!user?.id) {
      return { success: false, message: 'Please login first' };
    }

    try {
      console.log('Creating order for user:', user.id, 'with address:', addressId);
      const response = await orderService.createOrder(user.id, addressId);
      
      if (response.success) {
        console.log('Order created successfully');
        
        // Refresh orders and cart data
        await fetchOrders();
        await fetchCart(); // This should clear the cart
        
        console.log('Order and cart data refreshed');
        
        return { 
          success: true, 
          message: 'Order placed successfully!',
          shouldNavigateToOrders: true 
        };
      }
      
      return response;
    } catch (error) {
      console.error('Error creating order:', error);
      return { success: false, message: 'Failed to create order' };
    }
  };

  return {
    orders,
    fetchOrders,
    createOrder
  };
};