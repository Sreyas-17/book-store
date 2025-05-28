import React, { useState } from 'react';
import { Book, Search, Heart, ShoppingCart, User } from 'lucide-react';

const Header = ({ 
  user, 
  cart, 
  wishlist, 
  searchTerm, 
  setCurrentPage, 
  setSearchTerm, 
  searchBooks, 
  handleLogout 
}) => {
  const [searchType, setSearchType] = useState('title');
  
  console.log('Header render - User:', user);
  console.log('Header render - Cart length:', cart?.length || 0);
  console.log('Header render - Wishlist length:', wishlist?.length || 0);
  
  return (
    <header className="header">
      <div className="header-content">
        <div className="logo" onClick={() => setCurrentPage('home')}>
          <Book size={32} />
          <span>BookStore</span>
        </div>

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
                searchBooks(e.target.value, searchType);
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

        <nav className="nav">
          {user && user.id ? (
            <>
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

              <div className="dropdown">
                <button 
                  className="nav-item"
                  style={{background: 'none', border: 'none', cursor: 'pointer'}}
                >
                  <User size={20} />
                  <span>{user.firstName || 'User'}</span>
                </button>
                <div className="dropdown-menu">
                  <button
                    onClick={() => setCurrentPage('profile')}
                    className="dropdown-item"
                    style={{background: 'none', border: 'none', width: '100%'}}
                  >
                    Profile
                  </button>
                  <button
                    onClick={() => setCurrentPage('orders')}
                    className="dropdown-item"
                    style={{background: 'none', border: 'none', width: '100%'}}
                  >
                    Orders
                  </button>
                  <button
                    onClick={handleLogout}
                    className="dropdown-item"
                    style={{background: 'none', border: 'none', width: '100%'}}
                  >
                    Logout
                  </button>
                </div>
              </div>
            </>
          ) : (
            <button
              onClick={() => setCurrentPage('login')}
              className="btn btn-primary"
            >
              Sign In
            </button>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;