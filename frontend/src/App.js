import React, { useEffect } from 'react';
import { AppProvider, useAuth, useCart, useWishlist } from './contexts/AppContext';
import { useBooks } from './hooks/useBooks';
import { useNavigation } from './hooks/useNavigation';
import { useAddresses } from './hooks/useAddresses';
import { useOrders } from './hooks/useOrders';

import Header from './components/Header';
import HomePage from './components/HomePage';
import LoginPage from './components/LoginPage';
import RegisterPage from './components/RegisterPage';
import CartPage from './components/CartPage';
import CheckoutPage from './components/CheckoutPage';
import WishlistPage from './components/WishlistPage';
import OrdersPage from './components/OrdersPage';
import ProfilePage from './components/ProfilePage';

// Import new role-based components
import AdminDashboard from './components/AdminDashboard';
import VendorDashboard from './components/VendorDashboard';
import VendorRegistration from './components/VendorRegistration';

// Create separate components for protected routes
const AdminRoute = ({ user, navigation, ...props }) => {
  useEffect(() => {
    if (!user || user.role !== 'ADMIN') {
      navigation.navigateTo('home');
    }
  }, [user?.id, user?.role, navigation.navigateTo]);

  if (!user || user.role !== 'ADMIN') {
    return <div>Redirecting...</div>;
  }

  return <AdminDashboard {...props} />;
};

const VendorRoute = ({ user, navigation, ...props }) => {
  useEffect(() => {
    if (!user || user.role !== 'VENDOR') {
      navigation.navigateTo('home');
    }
  }, [user?.id, user?.role, navigation.navigateTo]);

  if (!user || user.role !== 'VENDOR') {
    return <div>Redirecting...</div>;
  }

  return <VendorDashboard {...props} />;
};

const AppContent = () => {
  const auth = useAuth();
  const cart = useCart();
  const wishlist = useWishlist();
  const books = useBooks();
  const navigation = useNavigation();
  const addresses = useAddresses();
  const orders = useOrders();

  // Navigate to appropriate dashboard when user logs in - FIXED: Removed navigation from deps
  useEffect(() => {
    if (auth.user && navigation.currentPage === 'login') {
      console.log('User logged in, determining navigation...');

      // Navigate based on user role
      if (auth.user.role === 'ADMIN') {
        navigation.navigateTo('admin-dashboard');
      } else if (auth.user.role === 'VENDOR') {
        navigation.navigateTo('vendor-dashboard');
      } else {
        navigation.navigateTo('home');
      }
    }
  }, [auth.user?.id, navigation.currentPage]); // FIXED: Only specific values

  // Navigate to home when user logs out - FIXED: Removed navigation from deps
  useEffect(() => {
    if (!auth.user && auth.isInitialized &&
        navigation.currentPage !== 'login' &&
        navigation.currentPage !== 'register' &&
        navigation.currentPage !== 'vendor-register') {
      console.log('User logged out, navigating to home...');
      navigation.navigateTo('home');
    }
  }, [auth.user?.id, auth.isInitialized, navigation.currentPage]); // FIXED: Only specific values

  // Clear role-specific data when user logs out - FIXED: Removed objects from deps
  useEffect(() => {
    if (!auth.user) {
      // Clear cart, wishlist when logged out
      cart.clearCart();
      wishlist.clearWishlist();
    }
  }, [auth.user?.id]); // FIXED: Only user ID

  const appProps = {
    ...auth,
    ...cart,
    ...wishlist,
    ...books,
    ...navigation,
    ...addresses,
    ...orders,
    setCurrentPage: navigation.navigateTo
  };

  // Enhanced page renderer with role-based routing
  const renderCurrentPage = () => {
    switch (navigation.currentPage) {
      case 'login':
        return <LoginPage {...appProps} />;

      case 'register':
        return <RegisterPage {...appProps} />;

      case 'vendor-register':
        return <VendorRegistration {...appProps} />;

      case 'cart':
        return <CartPage {...appProps} />;

      case 'checkout':
        return <CheckoutPage {...appProps} />;

      case 'wishlist':
        return <WishlistPage {...appProps} />;

      case 'orders':
        return <OrdersPage {...appProps} />;

      case 'profile':
        return <ProfilePage {...appProps} />;

      // Role-based dashboard routing with protected components
      case 'admin-dashboard':
        return <AdminRoute user={auth.user} navigation={navigation} {...appProps} />;

      case 'vendor-dashboard':
        return <VendorRoute user={auth.user} navigation={navigation} {...appProps} />;

      default:
        return <HomePage {...appProps} />;
    }
  };

  // Show loading state during initialization
  if (!auth.isInitialized) {
    return (
      <div style={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: '18px',
        color: '#6b7280',
        flexDirection: 'column',
        gap: '16px'
      }}>
        <div className="loading-spinner" style={{
          width: '40px',
          height: '40px',
          border: '4px solid #f3f4f6',
          borderTop: '4px solid #2563eb',
          borderRadius: '50%',
          animation: 'spin 1s linear infinite'
        }}></div>
        <div>Loading BookStore...</div>
      </div>
    );
  }

  // Pages that don't need header
  const noHeaderPages = ['login', 'register', 'vendor-register'];
  const showHeader = !noHeaderPages.includes(navigation.currentPage);

  return (
    <div style={{ minHeight: '100vh', backgroundColor: '#f9fafb' }}>
      {/* Add spinner animation CSS */}
      <style>{`
        @keyframes spin {
          0% { transform: rotate(0deg); }
          100% { transform: rotate(360deg); }
        }
      `}</style>

      {showHeader && <Header {...appProps} />}
      {renderCurrentPage()}
    </div>
  );
};

const App = () => {
  return (
    <AppProvider>
      <AppContent />
    </AppProvider>
  );
};

export default App;