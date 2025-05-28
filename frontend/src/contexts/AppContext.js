// src/contexts/AppContext.js
import React from 'react';
import { AuthProvider } from './AuthContext';
import { CartProvider } from './CartContext';
import { WishlistProvider } from './WishlistContext';

// Combined provider that wraps all contexts
export const AppProvider = ({ children }) => {
  return (
    <AuthProvider>
      <CartProvider>
        <WishlistProvider>
          {children}
        </WishlistProvider>
      </CartProvider>
    </AuthProvider>
  );
};

// Export all contexts for easy importing
export { useAuth } from './AuthContext';
export { useCart } from './CartContext';
export { useWishlist } from './WishlistContext';