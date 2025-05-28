import React from 'react';
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

const AppContent = () => {
  const auth = useAuth();
  const cart = useCart();
  const wishlist = useWishlist();
  const books = useBooks();
  const navigation = useNavigation();
  const addresses = useAddresses();
  const orders = useOrders();

  // Navigate to home when user logs in
  React.useEffect(() => {
    if (auth.user && navigation.currentPage === 'login') {
      console.log('User logged in, navigating to home...');
      navigation.navigateTo('home');
    }
  }, [auth.user, navigation.currentPage]);

  // Navigate to home when user logs out
  React.useEffect(() => {
    if (!auth.user && auth.isInitialized && navigation.currentPage !== 'login' && navigation.currentPage !== 'register') {
      console.log('User logged out, navigating to home...');
      navigation.navigateTo('home');
    }
  }, [auth.user, auth.isInitialized]);

  // Show success message when cart is empty after order
  React.useEffect(() => {
    if (cart.cart && cart.cart.length === 0 && navigation.currentPage === 'orders') {
      console.log('Cart cleared after order placement');
    }
  }, [cart.cart, navigation.currentPage]);

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

  const renderCurrentPage = () => {
    switch (navigation.currentPage) {
      case 'login':
        return <LoginPage {...appProps} />;
      case 'register':
        return <RegisterPage {...appProps} />;
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
      default:
        return <HomePage {...appProps} />;
    }
  };

  if (!auth.isInitialized) {
    return (
      <div style={{ 
        minHeight: '100vh', 
        display: 'flex', 
        alignItems: 'center', 
        justifyContent: 'center',
        fontSize: '18px',
        color: '#6b7280'
      }}>
        <div>Loading BookStore...</div>
      </div>
    );
  }

  return (
    <div style={{ minHeight: '100vh', backgroundColor: '#f9fafb' }}>
      {navigation.currentPage !== 'login' && navigation.currentPage !== 'register' && (
        <Header {...appProps} />
      )}
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