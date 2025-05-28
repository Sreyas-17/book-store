// src/components/Header.js - Fixed version with proper user state handling
import React from 'react';
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
  
  // Debug logging
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
          <Search className="search-icon" size={20} />
          <input
            type="text"
            placeholder="Search books..."
            value={searchTerm}
            onChange={(e) => {
              setSearchTerm(e.target.value);
              searchBooks(e.target.value);
            }}
            className="search-input"
          />
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