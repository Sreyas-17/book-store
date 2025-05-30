import { useState } from 'react';

export const useNavigation = () => {
  const [currentPage, setCurrentPage] = useState('home');

  const navigateTo = (page) => {
        console.log('navigateTo called with page:', page);
        setCurrentPage(page);
  };
  const goToLogin = () => setCurrentPage('login');
  const goToRegister = () => setCurrentPage('register');
  const goToHome = () => setCurrentPage('home');
  const goToCart = () => setCurrentPage('cart');
  const goToWishlist = () => setCurrentPage('wishlist');
  const goToCheckout = () => setCurrentPage('checkout');
  const goToOrders = () => setCurrentPage('orders');
  const goToProfile = () => setCurrentPage('profile');


  return {
    currentPage,
    navigateTo,
    goToLogin,
    goToRegister,
    goToHome,
    goToCart,
    goToWishlist,
    goToCheckout,
    goToOrders,
    goToProfile
  };
};