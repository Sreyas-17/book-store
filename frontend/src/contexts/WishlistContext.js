// src/contexts/WishlistContext.js
import React, { createContext, useContext, useState, useEffect } from 'react';
import { wishlistService } from '../services/wishlistService';
import { useAuth } from './AuthContext';

const WishlistContext = createContext();

export const useWishlist = () => {
  const context = useContext(WishlistContext);
  if (!context) {
    throw new Error('useWishlist must be used within WishlistProvider');
  }
  return context;
};

export const WishlistProvider = ({ children }) => {
  const [wishlistItems, setWishlistItems] = useState([]); // Initialize as empty array
  const [loading, setLoading] = useState(false);
  const { isAuthenticated } = useAuth();
  const [wishlist, setWishlist] = useState([]);
  const { user } = useAuth();

  useEffect(() => {
    if (user?.id) {
      fetchWishlist();
    } else {
      setWishlist([]); // Clear wishlist when user logs out
    }
  }, [user?.id]);

  const fetchWishlist = async (userId = user?.id) => {
    if (!userId) return;
    
    try {
      console.log('Fetching wishlist for user:', userId);
      const response = await wishlistService.getWishlist(userId);
      if (response.success) {
        setWishlist(response.data);
        console.log('Wishlist fetched:', response.data.length, 'items');
      }
    } catch (error) {
      console.error('Error fetching wishlist:', error);
    }
  };

  const addToWishlist = async (bookId) => {
    if (!user?.id) {
      return { success: false, message: 'Please login first' };
    }
    
    try {
      console.log('Adding to wishlist:', { bookId, userId: user.id });
      const response = await wishlistService.addToWishlist(user.id, bookId);
      
      if (response.success) {
        await fetchWishlist();
        console.log('Wishlist refreshed after adding item');
        return response;
      }
      
      return response;
    } catch (error) {
      console.error('Error adding to wishlist:', error);
      return { success: false, message: 'Failed to add to wishlist' };
    }
  };

  const removeFromWishlist = async (bookId) => {
    if (!user?.id) return;
    
    try {
      console.log('Removing from wishlist:', bookId);
      const response = await wishlistService.removeFromWishlist(user.id, bookId);
      
      if (response.success) {
        await fetchWishlist();
        console.log('Item removed from wishlist');
      }
      return response;
    } catch (error) {
      console.error('Error removing from wishlist:', error);
    }
  };

  const isInWishlist = (bookId) => {
    return wishlist.some(item => item.book && item.book.id === bookId);
  };

  const clearWishlist = () => {
    setWishlist([]);
  };

  const value = {
    wishlist,
    fetchWishlist,
    addToWishlist,
    removeFromWishlist,
    isInWishlist,
    clearWishlist
  };

  return (
    <WishlistContext.Provider value={value}>
      {children}
    </WishlistContext.Provider>
  );
};