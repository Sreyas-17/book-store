import React from 'react';
import { Heart } from 'lucide-react';
import BookCard from './BookCard';

const WishlistPage = ({ 
  user, 
  wishlist, 
  setCurrentPage,
  addToCart,
  updateCartQuantity,
  addToWishlist,
  removeFromWishlist,
  isInWishlist,
  getCartItemQuantity
}) => {
  if (!user) {
    return (
      <div className="empty-state">
        <Heart className="empty-icon" size={64} />
        <h2 className="empty-title">Please sign in to view your wishlist</h2>
        <button
          onClick={() => setCurrentPage('login')}
          className="btn btn-primary"
        >
          Sign In
        </button>
      </div>
    );
  }

  if (wishlist.length === 0) {
    return (
      <div className="empty-state">
        <Heart className="empty-icon" size={64} />
        <h2 className="empty-title">Your wishlist is empty</h2>
        <p className="empty-description">Save books you love for later!</p>
        <button
          onClick={() => setCurrentPage('home')}
          className="btn btn-primary"
        >
          Browse Books
        </button>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="page-header">
        <h2 className="page-title">My Wishlist</h2>
        <p className="page-subtitle">Books you've saved for later</p>
      </div>
      
      <div className="book-grid">
        {wishlist.map((item) => (
          <BookCard
            key={item.id}
            book={item.book}
            user={user}
            addToCart={addToCart}
            updateCartQuantity={updateCartQuantity}
            addToWishlist={addToWishlist}
            removeFromWishlist={removeFromWishlist}
            isInWishlist={isInWishlist}
            getCartItemQuantity={getCartItemQuantity}
          />
        ))}
      </div>
    </div>
  );
};

export default WishlistPage;