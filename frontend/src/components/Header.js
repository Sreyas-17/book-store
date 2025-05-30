// src/components/Header.js - Fixed JSX structure
import React, { useState } from 'react';
import { Book, Search, Heart, ShoppingCart, User, Settings, Store, Users } from 'lucide-react';

const Header = ({
  user,
  cart,
  wishlist,
  searchTerm,
  setCurrentPage,
  setSearchTerm,
  searchBooks,
  searchByAuthor,
  handleLogout
}) => {
  const [searchType, setSearchType] = useState('title');
  const [showUserMenu, setShowUserMenu] = useState(false);

  console.log('Header render - User:', user);
  console.log('Header render - Cart length:', cart?.length || 0);
  console.log('Header render - Wishlist length:', wishlist?.length || 0);

  // Close dropdown when clicking outside
  React.useEffect(() => {
    const handleClickOutside = () => setShowUserMenu(false);
    document.addEventListener('click', handleClickOutside);
    return () => document.removeEventListener('click', handleClickOutside);
  }, []);

  const getUserDisplayName = () => {
    if (!user) return 'Guest';

    // For vendors, show business name if available
    if (user.role === 'VENDOR' && user.businessName) {
      return user.businessName;
    }

    // For other users, show first name
    return user.firstName || 'User';
  };

  const getRoleIcon = () => {
    switch (user?.role) {
      case 'ADMIN':
        return <Users size={16} style={{ color: '#dc2626' }} />;
      case 'VENDOR':
        return <Store size={16} style={{ color: '#7c3aed' }} />;
      default:
        return <User size={16} />;
    }
  };

  const getRoleBadge = () => {
    if (!user?.role || user.role === 'USER') return null;

    return (
      <span style={{
        fontSize: '10px',
        backgroundColor: user.role === 'ADMIN' ? '#dc2626' : '#7c3aed',
        color: 'white',
        padding: '2px 6px',
        borderRadius: '8px',
        marginLeft: '4px'
      }}>
        {user.role}
      </span>
    );
  };

  return (
    <header className="header">
      <div className="header-content">
        <div className="logo" onClick={() => setCurrentPage('home')}>
          <Book size={32} />
          <span>BookStore</span>
        </div>

        {/* Search Bar */}
        <div className="search-container">
          <div className="search-wrapper" style={{
            display: 'flex',
            alignItems: 'center',
            backgroundColor: '#f5f5f5',
            borderRadius: '8px',
            padding: '8px 16px',
            width: '100%',
            maxWidth: '700px',
            border: '1px solid #e0e0e0',
            height: '48px'
          }}>
            <select
              value={searchType}
              onChange={(e) => setSearchType(e.target.value)}
              className="search-type-select"
              style={{
                padding: '8px 12px',
                marginRight: '8px',
                borderRadius: '4px',
                border: 'none',
                backgroundColor: 'transparent',
                color: '#333',
                fontSize: '15px',
                cursor: 'pointer',
                outline: 'none',
                width: '160px',
                height: '36px'
              }}
            >
              <option value="title">Title</option>
              <option value="author">Author</option>
            </select>
            <div style={{ width: '1px', height: '28px', backgroundColor: '#e0e0e0', margin: '0 12px' }}></div>
            <Search className="search-icon" size={18} style={{ color: '#666', marginRight: '16px' }} />
            <input
              type="text"
              placeholder={`Search by ${searchType}`}
              value={searchTerm}
              onChange={(e) => {
                setSearchTerm(e.target.value);
                if (searchType === 'title') {
                  searchBooks(e.target.value);
                } else {
                  searchByAuthor(e.target.value);
                }
              }}
              className="search-input"
              style={{
                border: 'none',
                backgroundColor: 'transparent',
                padding: '8px 12px',
                width: '100%',
                fontSize: '15px',
                outline: 'none',
                height: '36px',
                marginLeft: '4px'
              }}
            />
          </div>
        </div>

        {/* Navigation */}
        <nav className="nav">
          {user && user.id ? (
            <>
              {/* Role-specific dashboard links */}
              {user.role === 'ADMIN' && (
                <button
                  onClick={() => setCurrentPage('admin-dashboard')}
                  className="nav-item"
                  style={{
                    background: 'none',
                    border: 'none',
                    cursor: 'pointer',
                    color: '#dc2626',
                    fontWeight: '500'
                  }}
                >
                  <Users size={20} />
                  <span>Admin Panel</span>
                </button>
              )}

              {user.role === 'VENDOR' && (
                <button
                  onClick={() => setCurrentPage('vendor-dashboard')}
                  className="nav-item"
                  style={{
                    background: 'none',
                    border: 'none',
                    cursor: 'pointer',
                    color: '#7c3aed',
                    fontWeight: '500'
                  }}
                >
                  <Store size={20} />
                  <span>My Store</span>
                </button>
              )}

              {/* Regular user navigation - show for all authenticated users */}
              <button
                onClick={() => setCurrentPage('wishlist')}
                className="nav-item"
                style={{background: 'none', border: 'none', cursor: 'pointer'}}
              >
                <Heart size={20} />
                <span>Wishlist</span>
                {wishlist && wishlist.length > 0 && (
                  <span className="nav-badge">{wishlist.length}</span>
                )}
              </button>

              <button
                onClick={() => setCurrentPage('cart')}
                className="nav-item"
                style={{background: 'none', border: 'none', cursor: 'pointer'}}
              >
                <ShoppingCart size={20} />
                <span>Cart</span>
                {cart && cart.length > 0 && (
                  <span className="nav-badge">{cart.length}</span>
                )}
              </button>

              {/* User Dropdown Menu */}
              <div className="dropdown" style={{ position: 'relative' }}>
                <button
                  className="nav-item"
                  onClick={(e) => {
                    e.stopPropagation();
                    setShowUserMenu(!showUserMenu);
                  }}
                  style={{
                    background: 'none',
                    border: 'none',
                    cursor: 'pointer',
                    display: 'flex',
                    alignItems: 'center',
                    gap: '8px'
                  }}
                >
                  {getRoleIcon()}
                  <span>{getUserDisplayName()}</span>
                  {getRoleBadge()}
                </button>

                {showUserMenu && (
                  <div
                    className="dropdown-menu"
                    style={{
                      position: 'absolute',
                      top: '100%',
                      right: '0',
                      backgroundColor: 'white',
                      border: '1px solid #e5e7eb',
                      borderRadius: '8px',
                      boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.1)',
                      zIndex: 50,
                      minWidth: '200px',
                      padding: '8px 0',
                      marginTop: '8px'
                    }}
                  >
                    {/* Profile Section */}
                    <div style={{
                      padding: '12px 16px',
                      borderBottom: '1px solid #e5e7eb',
                      backgroundColor: '#f9fafb'
                    }}>
                      <div style={{ fontSize: '14px', fontWeight: '600', color: '#1f2937' }}>
                        {user.firstName} {user.lastName}
                      </div>
                      <div style={{ fontSize: '12px', color: '#6b7280' }}>
                        {user.email}
                      </div>
                      {user.role !== 'USER' && (
                        <div style={{
                          fontSize: '11px',
                          color: user.role === 'ADMIN' ? '#dc2626' : '#7c3aed',
                          fontWeight: '500',
                          marginTop: '4px'
                        }}>
                          {user.role === 'ADMIN' ? '👑 Administrator' : '🏪 Vendor Account'}
                        </div>
                      )}
                    </div>

                    {/* Menu Items */}
                    <button
                      onClick={() => {
                        setCurrentPage('profile');
                        setShowUserMenu(false);
                      }}
                      className="dropdown-item"
                      style={{
                        background: 'none',
                        border: 'none',
                        width: '100%',
                        textAlign: 'left',
                        padding: '12px 16px',
                        display: 'flex',
                        alignItems: 'center',
                        gap: '12px',
                        cursor: 'pointer',
                        fontSize: '14px'
                      }}
                      onMouseEnter={(e) => e.target.style.backgroundColor = '#f3f4f6'}
                      onMouseLeave={(e) => e.target.style.backgroundColor = 'transparent'}
                    >
                      <User size={16} />
                      My Profile
                    </button>

                    <button
                      onClick={() => {
                        setCurrentPage('orders');
                        setShowUserMenu(false);
                      }}
                      className="dropdown-item"
                      style={{
                        background: 'none',
                        border: 'none',
                        width: '100%',
                        textAlign: 'left',
                        padding: '12px 16px',
                        display: 'flex',
                        alignItems: 'center',
                        gap: '12px',
                        cursor: 'pointer',
                        fontSize: '14px'
                      }}
                      onMouseEnter={(e) => e.target.style.backgroundColor = '#f3f4f6'}
                      onMouseLeave={(e) => e.target.style.backgroundColor = 'transparent'}
                    >
                      <ShoppingCart size={16} />
                      My Orders
                    </button>

                    {/* Role-specific menu items */}
                    {user.role === 'ADMIN' && (
                      <button
                        onClick={() => {
                          setCurrentPage('admin-dashboard');
                          setShowUserMenu(false);
                        }}
                        className="dropdown-item"
                        style={{
                          background: 'none',
                          border: 'none',
                          width: '100%',
                          textAlign: 'left',
                          padding: '12px 16px',
                          display: 'flex',
                          alignItems: 'center',
                          gap: '12px',
                          cursor: 'pointer',
                          fontSize: '14px',
                          color: '#dc2626'
                        }}
                        onMouseEnter={(e) => e.target.style.backgroundColor = '#fef2f2'}
                        onMouseLeave={(e) => e.target.style.backgroundColor = 'transparent'}
                      >
                        <Settings size={16} />
                        Admin Dashboard
                      </button>
                    )}

                    {user.role === 'VENDOR' && (
                      <button
                        onClick={() => {
                          setCurrentPage('vendor-dashboard');
                          setShowUserMenu(false);
                        }}
                        className="dropdown-item"
                        style={{
                          background: 'none',
                          border: 'none',
                          width: '100%',
                          textAlign: 'left',
                          padding: '12px 16px',
                          display: 'flex',
                          alignItems: 'center',
                          gap: '12px',
                          cursor: 'pointer',
                          fontSize: '14px',
                          color: '#7c3aed'
                        }}
                        onMouseEnter={(e) => e.target.style.backgroundColor = '#faf5ff'}
                        onMouseLeave={(e) => e.target.style.backgroundColor = 'transparent'}
                      >
                        <Store size={16} />
                        Vendor Dashboard
                      </button>
                    )}

                    {/* Logout */}
                    <div style={{ borderTop: '1px solid #e5e7eb', marginTop: '8px', paddingTop: '8px' }}>
                      <button
                        onClick={() => {
                          handleLogout();
                          setShowUserMenu(false);
                        }}
                        className="dropdown-item"
                        style={{
                          background: 'none',
                          border: 'none',
                          width: '100%',
                          textAlign: 'left',
                          padding: '12px 16px',
                          display: 'flex',
                          alignItems: 'center',
                          gap: '12px',
                          cursor: 'pointer',
                          fontSize: '14px',
                          color: '#dc2626'
                        }}
                        onMouseEnter={(e) => e.target.style.backgroundColor = '#fef2f2'}
                        onMouseLeave={(e) => e.target.style.backgroundColor = 'transparent'}
                      >
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"></path>
                          <polyline points="16,17 21,12 16,7"></polyline>
                          <line x1="21" y1="12" x2="9" y2="12"></line>
                        </svg>
                        Sign Out
                      </button>
                    </div>
                  </div>
                )}
              </div>
            </>
          ) : (
            /* Not logged in */
            <div style={{ display: 'flex', gap: '12px' }}>
              <button
                onClick={() => {
                  console.log('Sign In button clicked!');
                  setCurrentPage('login');
                }}
                className="btn btn-outline"
              >
                Sign In
              </button>
              <button
                onClick={() => setCurrentPage('register')}
                className="btn btn-primary"
                style={{ padding: '8px 16px' }}
              >
                Sign Up
              </button>
              <button
                onClick={() => setCurrentPage('vendor-register')}
                className="btn btn-secondary"
                style={{
                  padding: '8px 16px',
                  backgroundColor: '#7c3aed',
                  color: 'white',
                  border: 'none'
                }}
              >
                Become a Vendor
              </button>
            </div>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;