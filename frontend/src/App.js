// src/App.js - Fixed user data fetching after login
import React, { useState, useEffect } from 'react';
import Header from './components/Header';
import HomePage from './components/HomePage';
import LoginPage from './components/LoginPage';
import RegisterPage from './components/RegisterPage';
import CartPage from './components/CartPage';
import CheckoutPage from './components/CheckoutPage';
import WishlistPage from './components/WishlistPage';
import OrdersPage from './components/OrdersPage';
import ProfilePage from './components/ProfilePage';
import { apiCall } from './utils/api';

const App = () => {
  const [currentPage, setCurrentPage] = useState('home');
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [books, setBooks] = useState([]);
  const [cart, setCart] = useState([]);
  const [wishlist, setWishlist] = useState([]);
  const [addresses, setAddresses] = useState([]);
  const [orders, setOrders] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);
  const [isInitialLoad, setIsInitialLoad] = useState(true);

  // Initialize app on first load
  useEffect(() => {
    const initializeApp = async () => {
      console.log('🚀 Initializing app...');
      
      // Always fetch books first
      await fetchBooks();
      
      // If we have a token, try to get user profile
      if (token) {
        console.log('🔑 Token found, fetching user profile...');
        const userData = await fetchUserProfile();
        
        if (userData) {
          console.log('✅ User profile loaded:', userData.email);
          // Fetch user-specific data immediately after user is loaded
          await fetchUserSpecificData(userData);
        } else {
          console.log('❌ Failed to load user profile, clearing token');
          handleLogout();
        }
      } else {
        console.log('ℹ️ No token found, user not logged in');
      }
      
      setIsInitialLoad(false);
    };

    initializeApp();
  }, []); // Only run once on mount

  // Fetch user-specific data
  const fetchUserSpecificData = async (userData = user) => {
    if (!userData || !userData.id) {
      console.log('⚠️ No user data available for fetching user-specific data');
      return;
    }
    
    console.log('📊 Fetching user-specific data for user:', userData.id);
    
    try {
      await Promise.all([
        fetchCart(userData.id),
        fetchWishlist(userData.id),
        fetchAddresses(userData.id),
        fetchOrders(userData.id)
      ]);
      console.log('✅ All user data fetched successfully');
    } catch (error) {
      console.error('❌ Error fetching user data:', error);
    }
  };

  // API Functions
  const fetchUserProfile = async () => {
    if (!token) {
      console.log('🚫 No token available for user profile');
      return null;
    }
    
    try {
      console.log('👤 Fetching user profile...');
      const response = await apiCall('/auth/profile', {}, token);
      
      if (response.success && response.data) {
        console.log('✅ User profile fetched successfully:', response.data.email);
        setUser(response.data);
        return response.data;
      } else {
        console.error('❌ Failed to fetch user profile:', response.message);
        return null;
      }
    } catch (error) {
      console.error('💥 Error fetching user profile:', error);
      return null;
    }
  };

  const fetchBooks = async () => {
    try {
      const response = await apiCall('/books/all');
      if (response.success) {
        setBooks(response.data);
        console.log('📚 Books fetched:', response.data.length);
      }
    } catch (error) {
      console.error('Error fetching books:', error);
    }
  };

  const fetchCart = async (userId = user?.id) => {
    if (!userId) {
      console.log('🛒 No user ID for cart fetch');
      return;
    }
    
    try {
      console.log('🛒 Fetching cart for user:', userId);
      const response = await apiCall(`/cart/${userId}`, {}, token);
      if (response.success) {
        setCart(response.data);
        console.log('✅ Cart fetched:', response.data.length, 'items');
      } else {
        console.log('⚠️ Cart fetch failed:', response.message);
      }
    } catch (error) {
      console.error('Error fetching cart:', error);
    }
  };

  const fetchWishlist = async (userId = user?.id) => {
    if (!userId) {
      console.log('❤️ No user ID for wishlist fetch');
      return;
    }
    
    try {
      console.log('❤️ Fetching wishlist for user:', userId);
      const response = await apiCall(`/wishlist/${userId}`, {}, token);
      if (response.success) {
        setWishlist(response.data);
        console.log('✅ Wishlist fetched:', response.data.length, 'items');
      } else {
        console.log('⚠️ Wishlist fetch failed:', response.message);
      }
    } catch (error) {
      console.error('Error fetching wishlist:', error);
    }
  };

  const fetchAddresses = async (userId = user?.id) => {
    if (!userId) return;
    
    try {
      const response = await apiCall(`/addresses/user/${userId}`, {}, token);
      if (response.success) {
        setAddresses(response.data);
        console.log('🏠 Addresses fetched:', response.data.length, 'items');
      }
    } catch (error) {
      console.error('Error fetching addresses:', error);
    }
  };

  const fetchOrders = async (userId = user?.id) => {
    if (!userId) return;
    
    try {
      const response = await apiCall(`/orders/user/${userId}`, {}, token);
      if (response.success) {
        setOrders(response.data);
        console.log('📦 Orders fetched:', response.data.length, 'items');
      }
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };

  // Auth Functions
  const handleLogin = async (email, password) => {
    setLoading(true);
    
    try {
      console.log('🔐 Attempting login for:', email);
      const response = await apiCall('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      });
      
      console.log('📥 Login response:', response);
      
      if (response.success && response.data) {
        const newToken = response.data;
        console.log('✅ Login successful, token received');
        
        // Store token immediately
        setToken(newToken);
        localStorage.setItem('token', newToken);
        
        // Fetch user profile with new token
        const userResponse = await apiCall('/auth/profile', {}, newToken);
        console.log('👤 Profile fetch response:', userResponse);
        
        if (userResponse.success && userResponse.data) {
          setUser(userResponse.data);
          console.log('✅ User set successfully:', userResponse.data.email);
          
          // Fetch user-specific data immediately
          await fetchUserSpecificData(userResponse.data);
          
          // Navigate to home
          setCurrentPage('home');
          return { success: true };
        } else {
          console.error('❌ Failed to fetch user profile after login');
          return { success: false, message: 'Failed to load user profile' };
        }
      } else {
        console.log('❌ Login failed:', response.message);
        return response;
      }
    } catch (error) {
      console.error('💥 Login error:', error);
      return { success: false, message: 'Network error occurred' };
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (userData) => {
    setLoading(true);
    try {
      const response = await apiCall('/auth/register', {
        method: 'POST',
        body: JSON.stringify(userData),
      });
      console.log('📝 Register response:', response);
      return response;
    } catch (error) {
      console.error('Register error:', error);
      return { success: false, message: 'Network error occurred' };
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    console.log('🚪 Logging out user');
    setToken(null);
    setUser(null);
    setCart([]);
    setWishlist([]);
    setAddresses([]);
    setOrders([]);
    localStorage.removeItem('token');
    setCurrentPage('home');
  };

  // Cart Functions
  const addToCart = async (bookId, quantity = 1) => {
    console.log('🛒 Add to cart called:', { bookId, quantity, userId: user?.id });
    
    // Check if user is logged in
    if (!user || !user.id) {
      console.log('❌ User not logged in, redirecting to login');
      setCurrentPage('login');
      return { success: false, message: 'Please login first' };
    }
    
    try {
      console.log('➕ Adding to cart for user:', user.id);
      const response = await apiCall(`/cart/add?userId=${user.id}&bookId=${bookId}&quantity=${quantity}`, {
        method: 'POST',
      }, token);
      
      console.log('📥 Add to cart response:', response);
      
      if (response.success) {
        // Refresh cart data immediately
        await fetchCart(user.id);
        console.log('✅ Cart refreshed after adding item');
        return response;
      } else {
        console.error('❌ Failed to add to cart:', response.message);
        return response;
      }
    } catch (error) {
      console.error('💥 Error adding to cart:', error);
      return { success: false, message: 'Failed to add to cart' };
    }
  };

  const removeFromCart = async (bookId) => {
    if (!user?.id) return;
    
    try {
      const response = await apiCall(`/cart/remove?userId=${user.id}&bookId=${bookId}`, {
        method: 'DELETE',
      }, token);
      
      if (response.success) {
        await fetchCart(user.id);
        console.log('✅ Item removed from cart');
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
      const response = await apiCall(`/cart/update-quantity?userId=${user.id}&bookId=${bookId}&quantity=${quantity}`, {
        method: 'PUT',
      }, token);
      
      if (response.success) {
        await fetchCart(user.id);
        console.log('✅ Cart quantity updated');
      }
      return response;
    } catch (error) {
      console.error('Error updating cart quantity:', error);
    }
  };

  // Wishlist Functions
  const addToWishlist = async (bookId) => {
    console.log('❤️ Add to wishlist called:', { bookId, userId: user?.id });
    
    if (!user?.id) {
      console.log('❌ User not logged in, redirecting to login');
      setCurrentPage('login');
      return { success: false, message: 'Please login first' };
    }
    
    try {
      console.log('➕ Adding to wishlist for user:', user.id);
      const response = await apiCall(`/wishlist/add?userId=${user.id}&bookId=${bookId}`, {
        method: 'POST',
      }, token);
      
      console.log('📥 Add to wishlist response:', response);
      
      if (response.success) {
        await fetchWishlist(user.id);
        console.log('✅ Wishlist refreshed after adding item');
        return response;
      } else {
        console.error('❌ Failed to add to wishlist:', response.message);
        return response;
      }
    } catch (error) {
      console.error('💥 Error adding to wishlist:', error);
      return { success: false, message: 'Failed to add to wishlist' };
    }
  };

  const removeFromWishlist = async (bookId) => {
    if (!user?.id) return;
    
    try {
      console.log('➖ Removing from wishlist:', bookId);
      const response = await apiCall(`/wishlist/remove?userId=${user.id}&bookId=${bookId}`, {
        method: 'DELETE',
      }, token);
      
      if (response.success) {
        await fetchWishlist(user.id);
        console.log('✅ Item removed from wishlist');
      }
      return response;
    } catch (error) {
      console.error('Error removing from wishlist:', error);
    }
  };

  // Order Functions
  const createOrder = async (addressId) => {
    if (!user?.id) return;
    
    try {
      const response = await apiCall(`/orders/create?userId=${user.id}&addressId=${addressId}`, {
        method: 'POST',
      }, token);
      
      if (response.success) {
        await fetchCart(user.id);
        await fetchOrders(user.id);
        setCurrentPage('orders');
      }
      return response;
    } catch (error) {
      console.error('Error creating order:', error);
    }
  };

  // Search Function
  const searchBooks = async (term) => {
    if (!term.trim()) {
      fetchBooks();
      return;
    }
    
    try {
      const response = await apiCall(`/books/search/title?title=${term}`);
      if (response.success) {
        setBooks(response.data);
      }
    } catch (error) {
      console.error('Error searching books:', error);
    }
  };

  // Helper Functions
  const isInWishlist = (bookId) => {
  console.log('🔍 DEBUG - Checking wishlist for book:', bookId);
  console.log('🔍 DEBUG - Wishlist array:', wishlist);
  console.log('🔍 DEBUG - Wishlist length:', wishlist.length);
  
  // Log each wishlist item structure
  wishlist.forEach((item, index) => {
    console.log(`🔍 DEBUG - Wishlist item ${index}:`, item);
    console.log(`🔍 DEBUG - Item book:`, item.book);
    console.log(`🔍 DEBUG - Item book id:`, item.book?.id);
  });
  
  const result = wishlist.some(item => item.book && item.book.id === bookId);
  console.log('🔍 DEBUG - isInWishlist result:', result);
  return result;
};

  const getCartItemQuantity = (bookId) => {
    const item = cart.find(item => item.book && item.book.id === bookId);
    const quantity = item ? item.quantity : 0;
    console.log('🔍 getCartItemQuantity for book', bookId, ':', quantity);
    return quantity;
  };

  const getCartTotal = () => {
    return cart.reduce((total, item) => {
      if (item.book && item.book.price) {
        return total + (item.book.price * item.quantity);
      }
      return total;
    }, 0).toFixed(2);
  };

  // Props to pass to components
  const appProps = {
    user,
    token,
    books,
    cart,
    wishlist,
    addresses,
    orders,
    searchTerm,
    loading,
    setCurrentPage,
    setSearchTerm,
    setUser,
    handleLogin,
    handleRegister,
    handleLogout,
    addToCart,
    removeFromCart,
    updateCartQuantity,
    addToWishlist,
    removeFromWishlist,
    createOrder,
    searchBooks,
    isInWishlist,
    getCartItemQuantity,
    getCartTotal,
    fetchAddresses
  };

  // Debug info
  console.log('🎯 App render - User:', user?.email || 'Not logged in');
  console.log('🎯 App render - Cart items:', cart.length);
  console.log('🎯 App render - Wishlist items:', wishlist.length);

  // Main render logic
  const renderCurrentPage = () => {
    switch (currentPage) {
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

  return (
    <div style={{minHeight: '100vh', backgroundColor: '#f9fafb'}}>
      {currentPage !== 'login' && currentPage !== 'register' && (
        <Header {...appProps} />
      )}
      {renderCurrentPage()}
    </div>
  );
};

export default App;