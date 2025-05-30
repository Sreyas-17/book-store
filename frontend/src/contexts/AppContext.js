import React from 'react';
import { AuthProvider } from './AuthContext';
import { CartProvider } from './CartContext';
import { WishlistProvider } from './WishlistContext';
import { VendorProvider } from './VendorContext';
import { AdminProvider } from './AdminContext';

export const AppProvider = ({ children }) => {
  return (
    <AuthProvider>
      <VendorProvider>
        <AdminProvider>
          <CartProvider>
            <WishlistProvider>
              {children}
            </WishlistProvider>
          </CartProvider>
        </AdminProvider>
      </VendorProvider>
    </AuthProvider>
  );
};

// Export all contexts
export { useAuth } from './AuthContext';
export { useCart } from './CartContext';
export { useWishlist } from './WishlistContext';
export { useVendor } from './VendorContext';
export { useAdmin } from './AdminContext';