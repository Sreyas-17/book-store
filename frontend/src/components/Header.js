import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useCart } from '../contexts/CartContext';
import { useWishlist } from '../contexts/WishlistContext';
import { useBooks } from '../hooks/useBooks';

const Header = () => {
  const { user, isAuthenticated, handleLogout } = useAuth();
  const { cartItems = [] } = useCart();
  const { wishlistItems = [] } = useWishlist();
  const { searchBooks } = useBooks();
  const navigate = useNavigate();
  const location = useLocation();
  
  const [searchQuery, setSearchQuery] = useState('');
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isProfileDropdownOpen, setIsProfileDropdownOpen] = useState(false);

  useEffect(() => {
    setIsMenuOpen(false);
    setIsProfileDropdownOpen(false);
  }, [location.pathname]);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      searchBooks(searchQuery.trim());
      if (location.pathname !== '/') {
        navigate('/');
      }
    }
  };

  const handleLogoutClick = () => {
    handleLogout();
    navigate('/');
    setIsProfileDropdownOpen(false);
  };

  const cartItemCount = Array.isArray(cartItems) 
    ? cartItems.reduce((total, item) => total + (item?.quantity || 0), 0)
    : 0;
  
  const wishlistItemCount = Array.isArray(wishlistItems) 
    ? wishlistItems.length 
    : 0;

  const getDashboardLink = () => {
    if (user?.role === 'ADMIN') {
      return { path: '/admin', label: 'Admin Dashboard' };
    } else if (user?.role === 'VENDOR') {
      return { path: '/vendor', label: 'Vendor Dashboard' };
    }
    return null;
  };

  const dashboardLink = getDashboardLink();

  return (
    <header className="bg-gradient-to-r from-indigo-500 via-purple-500 to-purple-600 shadow-lg sticky top-0 z-50">
      <div className="max-w-6xl mx-auto flex items-center justify-between px-8 py-4 gap-8">
        {/* Logo */}
        <div className="flex-shrink-0">
          <Link to="/" className="text-white no-underline">
            <h1 className="text-3xl font-bold text-white m-0 drop-shadow-lg">BookStore</h1>
          </Link>
        </div>

        {/* Search Section */}
        <div className="flex-1 max-w-lg mx-8">
          <form onSubmit={handleSearch} className="w-full">
            <div className="flex bg-white rounded-full overflow-hidden shadow-lg">
              <input
                type="text"
                placeholder="Search books, authors..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="flex-1 px-5 py-3 border-none outline-none text-base bg-transparent placeholder-gray-400"
              />
              <button 
                type="submit"
                className="bg-indigo-500 text-white border-none px-5 py-3 cursor-pointer text-lg hover:bg-indigo-600 transition-colors duration-300"
              >
                🔍
              </button>
            </div>
          </form>
        </div>

        {/* Mobile Menu Toggle */}
        <button 
          className="md:hidden bg-transparent border-none cursor-pointer p-2 flex flex-col justify-center items-center"
          onClick={() => setIsMenuOpen(!isMenuOpen)}
        >
          <div className="flex flex-col w-6 h-5 relative">
            <span className={`block h-0.5 w-full bg-white my-0.5 transition-all duration-300 ${isMenuOpen ? 'transform rotate-45 translate-y-2' : ''}`}></span>
            <span className={`block h-0.5 w-full bg-white my-0.5 transition-all duration-300 ${isMenuOpen ? 'opacity-0' : ''}`}></span>
            <span className={`block h-0.5 w-full bg-white my-0.5 transition-all duration-300 ${isMenuOpen ? 'transform -rotate-45 -translate-y-2' : ''}`}></span>
          </div>
        </button>

        {/* Desktop Navigation */}
        <nav className="hidden md:flex items-center">
          <div className="flex items-center gap-6">
            <Link 
              to="/" 
              className={`text-white no-underline px-4 py-2 rounded-full transition-all duration-300 font-medium flex items-center gap-2 ${
                location.pathname === '/' 
                  ? 'bg-white bg-opacity-20 font-semibold' 
                  : 'hover:bg-white hover:bg-opacity-10 hover:-translate-y-0.5'
              }`}
            >
              Home
            </Link>

            {isAuthenticated ? (
              <>
                {/* Dashboard Link */}
                {dashboardLink && (
                  <Link 
                    to={dashboardLink.path} 
                    className="bg-white bg-opacity-15 border border-white border-opacity-30 text-white no-underline px-4 py-2 rounded-full transition-all duration-300 font-medium hover:bg-white hover:bg-opacity-25"
                  >
                    {dashboardLink.label}
                  </Link>
                )}

                {/* Cart */}
                <Link 
                  to="/cart" 
                  className="relative text-white no-underline px-4 py-2 rounded-full transition-all duration-300 font-medium flex items-center gap-2 hover:bg-white hover:bg-opacity-10 hover:-translate-y-0.5"
                >
                  <span className="text-xl">🛒</span>
                  <span className="text-sm">Cart</span>
                  {cartItemCount > 0 && (
                    <span className="absolute -top-2 -right-2 bg-green-500 text-white text-xs rounded-full min-w-5 h-5 flex items-center justify-center px-1.5 font-bold">
                      {cartItemCount}
                    </span>
                  )}
                </Link>

                {/* Wishlist */}
                <Link 
                  to="/wishlist" 
                  className="relative text-white no-underline px-4 py-2 rounded-full transition-all duration-300 font-medium flex items-center gap-2 hover:bg-white hover:bg-opacity-10 hover:-translate-y-0.5"
                >
                  <span className="text-xl">❤️</span>
                  <span className="text-sm">Wishlist</span>
                  {wishlistItemCount > 0 && (
                    <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full min-w-5 h-5 flex items-center justify-center px-1.5 font-bold">
                      {wishlistItemCount}
                    </span>
                  )}
                </Link>

                {/* Orders */}
                <Link 
                  to="/orders" 
                  className={`text-white no-underline px-4 py-2 rounded-full transition-all duration-300 font-medium flex items-center gap-2 ${
                    location.pathname === '/orders' 
                      ? 'bg-white bg-opacity-20 font-semibold' 
                      : 'hover:bg-white hover:bg-opacity-10 hover:-translate-y-0.5'
                  }`}
                >
                  Orders
                </Link>

                {/* Profile Dropdown */}
                <div className="relative">
                  <button 
                    className="bg-transparent border-none text-white cursor-pointer px-4 py-2 rounded-full transition-all duration-300 flex items-center gap-2 font-medium hover:bg-white hover:bg-opacity-10"
                    onClick={() => setIsProfileDropdownOpen(!isProfileDropdownOpen)}
                  >
                    <span className="text-xl">👤</span>
                    <span className="text-sm">{user?.name || 'Profile'}</span>
                    <span className={`text-xs transition-transform duration-300 ${isProfileDropdownOpen ? 'rotate-180' : ''}`}>▼</span>
                  </button>
                  
                  {isProfileDropdownOpen && (
                    <div className="absolute top-full right-0 bg-white rounded-xl shadow-2xl min-w-56 z-50 border border-gray-200 overflow-hidden mt-2">
                      <div className="px-4 py-4 bg-gradient-to-r from-gray-50 to-blue-50">
                        <p className="font-semibold text-gray-900 text-base m-0">{user?.name}</p>
                        <p className="text-sm text-gray-600 mt-0.5 m-0">{user?.email}</p>
                        {user?.role && (
                          <span className="inline-block mt-1 px-2 py-0.5 bg-blue-100 text-blue-800 text-xs font-medium rounded-full uppercase tracking-wide">
                            {user.role}
                          </span>
                        )}
                      </div>
                      <div className="h-px bg-gray-200"></div>
                      <Link 
                        to="/profile" 
                        className="flex items-center px-4 py-3 text-gray-700 no-underline transition-colors duration-200 hover:bg-gray-50 text-sm"
                        onClick={() => setIsProfileDropdownOpen(false)}
                      >
                        <span className="mr-3 text-base">⚙️</span>
                        Profile Settings
                      </Link>
                      <div className="h-px bg-gray-200"></div>
                      <button 
                        onClick={handleLogoutClick}
                        className="flex items-center w-full px-4 py-3 text-red-600 bg-transparent border-none cursor-pointer transition-colors duration-200 hover:bg-red-50 text-sm text-left"
                      >
                        <span className="mr-3 text-base">🚪</span>
                        Sign out
                      </button>
                    </div>
                  )}
                </div>
              </>
            ) : (
              <>
                <Link 
                  to="/login" 
                  className="text-white no-underline px-4 py-2 rounded-full transition-all duration-300 font-semibold hover:bg-white hover:bg-opacity-10"
                >
                  Sign in
                </Link>
                <Link 
                  to="/register" 
                  className="bg-white bg-opacity-15 border border-white border-opacity-30 text-white no-underline px-4 py-2 rounded-full transition-all duration-300 font-semibold hover:bg-white hover:bg-opacity-25"
                >
                  Sign up
                </Link>
              </>
            )}
          </div>
        </nav>

        {/* Mobile Navigation */}
        {isMenuOpen && (
          <div className="md:hidden absolute top-full left-0 right-0 bg-gradient-to-r from-indigo-500 via-purple-500 to-purple-600 px-4 py-4 transform transition-all duration-300">
            <div className="flex flex-col gap-2 w-full">
              <Link 
                to="/" 
                className="block text-white no-underline w-full justify-center px-3 py-3 text-center"
                onClick={() => setIsMenuOpen(false)}
              >
                Home
              </Link>
              
              {isAuthenticated ? (
                <>
                  {dashboardLink && (
                    <Link 
                      to={dashboardLink.path} 
                      className="block text-white no-underline w-full justify-center px-3 py-3 text-center"
                      onClick={() => setIsMenuOpen(false)}
                    >
                      {dashboardLink.label}
                    </Link>
                  )}
                  <Link 
                    to="/cart" 
                    className="block text-white no-underline w-full justify-center px-3 py-3 text-center"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Cart ({cartItemCount})
                  </Link>
                  <Link 
                    to="/wishlist" 
                    className="block text-white no-underline w-full justify-center px-3 py-3 text-center"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Wishlist ({wishlistItemCount})
                  </Link>
                  <Link 
                    to="/orders" 
                    className="block text-white no-underline w-full justify-center px-3 py-3 text-center"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Orders
                  </Link>
                  <Link 
                    to="/profile" 
                    className="block text-white no-underline w-full justify-center px-3 py-3 text-center"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Profile
                  </Link>
                  <button 
                    onClick={handleLogoutClick}
                    className="block w-full text-left text-red-200 bg-transparent border-none cursor-pointer px-3 py-3 text-center"
                  >
                    Sign out
                  </button>
                </>
              ) : (
                <>
                  <Link 
                    to="/login" 
                    className="block text-white no-underline w-full justify-center px-3 py-3 text-center"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Sign in
                  </Link>
                  <Link 
                    to="/register" 
                    className="block text-white no-underline w-full justify-center px-3 py-3 text-center"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Sign up
                  </Link>
                </>
              )}
            </div>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header;
