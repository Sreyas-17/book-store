import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useCart } from '../contexts/CartContext';
import { useWishlist } from '../contexts/WishlistContext';
import { useBooks } from '../hooks/useBooks';
import '../index.css';

const Header = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const { cartItems = [] } = useCart(); // Default to empty array
  const { wishlistItems = [] } = useWishlist(); // Default to empty array
  const { searchBooks } = useBooks();
  const navigate = useNavigate();
  const location = useLocation();
  
  const [searchQuery, setSearchQuery] = useState('');
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isProfileDropdownOpen, setIsProfileDropdownOpen] = useState(false);

  // Close mobile menu when route changes
  useEffect(() => {
    setIsMenuOpen(false);
    setIsProfileDropdownOpen(false);
  }, [location.pathname]);

  // Close dropdowns when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (!event.target.closest('.profile-dropdown') && !event.target.closest('.profile-menu-trigger')) {
        setIsProfileDropdownOpen(false);
      }
      if (!event.target.closest('.mobile-menu') && !event.target.closest('.menu-toggle')) {
        setIsMenuOpen(false);
      }
    };

    document.addEventListener('click', handleClickOutside);
    return () => document.removeEventListener('click', handleClickOutside);
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      searchBooks(searchQuery.trim());
      if (location.pathname !== '/') {
        navigate('/');
      }
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/');
    setIsProfileDropdownOpen(false);
  };

  const toggleMobileMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const toggleProfileDropdown = () => {
    setIsProfileDropdownOpen(!isProfileDropdownOpen);
  };

  // Safe calculation with defensive checks
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
    <header className="header">
      <div className="header-container">
        {/* Logo */}
        <div className="logo">
          <Link to="/" className="logo-link">
            <h1>BookStore</h1>
          </Link>
        </div>

        {/* Search Bar */}
        <div className="search-section">
          <form onSubmit={handleSearch} className="search-form">
            <input
              type="text"
              placeholder="Search books by title or author..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="search-input"
            />
            <button type="submit" className="search-button">
              <span className="search-icon">🔍</span>
            </button>
          </form>
        </div>

        {/* Mobile Menu Toggle */}
        <button 
          className="menu-toggle"
          onClick={toggleMobileMenu}
          aria-label="Toggle mobile menu"
        >
          <span className={`hamburger ${isMenuOpen ? 'active' : ''}`}>
            <span></span>
            <span></span>
            <span></span>
          </span>
        </button>

        {/* Navigation */}
        <nav className={`nav ${isMenuOpen ? 'nav-open' : ''}`}>
          <div className="nav-links">
            {/* Home Link */}
            <Link 
              to="/" 
              className={`nav-link ${location.pathname === '/' ? 'active' : ''}`}
            >
              Home
            </Link>

            {/* Authenticated User Links */}
            {isAuthenticated ? (
              <>
                {/* Dashboard Link (Role-based) */}
                {dashboardLink && (
                  <Link 
                    to={dashboardLink.path} 
                    className={`nav-link dashboard-link ${location.pathname === dashboardLink.path ? 'active' : ''}`}
                  >
                    {dashboardLink.label}
                  </Link>
                )}

                {/* Cart Link */}
                <Link 
                  to="/cart" 
                  className={`nav-link cart-link ${location.pathname === '/cart' ? 'active' : ''}`}
                >
                  <span className="nav-icon">🛒</span>
                  <span className="nav-text">Cart</span>
                  {cartItemCount > 0 && (
                    <span className="badge cart-badge">{cartItemCount}</span>
                  )}
                </Link>

                {/* Wishlist Link */}
                <Link 
                  to="/wishlist" 
                  className={`nav-link wishlist-link ${location.pathname === '/wishlist' ? 'active' : ''}`}
                >
                  <span className="nav-icon">❤️</span>
                  <span className="nav-text">Wishlist</span>
                  {wishlistItemCount > 0 && (
                    <span className="badge wishlist-badge">{wishlistItemCount}</span>
                  )}
                </Link>

                {/* Orders Link */}
                <Link 
                  to="/orders" 
                  className={`nav-link ${location.pathname === '/orders' ? 'active' : ''}`}
                >
                  Orders
                </Link>

                {/* Profile Dropdown */}
                <div className="profile-dropdown">
                  <button 
                    className="profile-menu-trigger"
                    onClick={toggleProfileDropdown}
                    aria-label="Profile menu"
                  >
                    <span className="profile-icon">👤</span>
                    <span className="profile-name">{user?.name || 'Profile'}</span>
                    <span className={`dropdown-arrow ${isProfileDropdownOpen ? 'open' : ''}`}>▼</span>
                  </button>
                  
                  {isProfileDropdownOpen && (
                    <div className="profile-dropdown-menu">
                      <div className="profile-info">
                        <div className="profile-name-full">{user?.name}</div>
                        <div className="profile-email">{user?.email}</div>
                        {user?.role && (
                          <div className="profile-role">{user.role}</div>
                        )}
                      </div>
                      <div className="dropdown-divider"></div>
                      <Link 
                        to="/profile" 
                        className="dropdown-link"
                        onClick={() => setIsProfileDropdownOpen(false)}
                      >
                        <span className="dropdown-icon">⚙️</span>
                        Profile Settings
                      </Link>
                      <button 
                        onClick={handleLogout}
                        className="dropdown-link logout-btn"
                      >
                        <span className="dropdown-icon">🚪</span>
                        Logout
                      </button>
                    </div>
                  )}
                </div>
              </>
            ) : (
              /* Guest User Links */
              <>
                <Link 
                  to="/login" 
                  className={`nav-link login-link ${location.pathname === '/login' ? 'active' : ''}`}
                >
                  Login
                </Link>
                <Link 
                  to="/register" 
                  className={`nav-link register-link ${location.pathname === '/register' ? 'active' : ''}`}
                >
                  Register
                </Link>
              </>
            )}
          </div>
        </nav>
      </div>
    </header>
  );
};

export default Header;
