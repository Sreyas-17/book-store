// src/contexts/CartContext.js
import React, { createContext, useContext, useState, useEffect } from 'react';
import { cartService } from '../services/cartService';
import { useAuth } from './AuthContext';

const CartContext = createContext();

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within CartProvider');
  }
  return context;
};

export const CartProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState([]); // Initialize as empty array
  const [loading, setLoading] = useState(false);
  const { isAuthenticated } = useAuth();
  const [cart, setCart] = useState([]);
  const { user } = useAuth();

  useEffect(() => {
    if (user?.id) {
      fetchCart();
    } else {
      setCart([]); // Clear cart when user logs out
    }
  }, [user?.id]);

  const fetchCart = async (userId = user?.id) => {
    if (!userId) return;
    
    try {
      console.log('Fetching cart for user:', userId);
      const response = await cartService.getCart(userId);
      if (response.success) {
        setCart(response.data);
        console.log('Cart fetched:', response.data.length, 'items');
      }
    } catch (error) {
      console.error('Error fetching cart:', error);
    }
  };

  const addToCart = async (bookId, quantity = 1) => {
    if (!user?.id) {
      return { success: false, message: 'Please login first' };
    }
    
    try {
      console.log('Adding to cart:', { bookId, quantity, userId: user.id });
      const response = await cartService.addToCart(user.id, bookId, quantity);
      
      if (response.success) {
        await fetchCart();
        console.log('Cart refreshed after adding item');
        return response;
      }
      
      return response;
    } catch (error) {
      console.error('Error adding to cart:', error);
      return { success: false, message: 'Failed to add to cart' };
    }
  };

  const removeFromCart = async (bookId) => {
    if (!user?.id) return;
    
    try {
      const response = await cartService.removeFromCart(user.id, bookId);
      if (response.success) {
        await fetchCart();
        console.log('Item removed from cart');
      }
      return response;
    } catch (error) {
      console.error('Error removing from cart:', error);
    }
  };

  const updateCartQuantity = async (bookId, quantity) => {
    if (!user?.id) return;
    
    if (quantity <= 0) {
      return removeFromCart(bookId);
    }
    
    try {
      const response = await cartService.updateQuantity(user.id, bookId, quantity);
      if (response.success) {
        await fetchCart();
        console.log('Cart quantity updated');
      }
      return response;
    } catch (error) {
      console.error('Error updating cart quantity:', error);
    }
  };

  const getCartItemQuantity = (bookId) => {
    const item = cart.find(item => item.book && item.book.id === bookId);
    return item ? item.quantity : 0;
  };

  const getCartTotal = () => {
    return cart.reduce((total, item) => {
      if (item.book && item.book.price) {
        return total + (item.book.price * item.quantity);
      }
      return total;
    }, 0).toFixed(2);
  };

  const clearCart = () => {
    setCart([]);
  };

  const value = {
    cart,
    fetchCart,
    addToCart,
    removeFromCart,
    updateCartQuantity,
    getCartItemQuantity,
    getCartTotal,
    clearCart
  };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
};