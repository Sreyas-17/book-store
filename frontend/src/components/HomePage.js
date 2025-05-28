import React from 'react';
import { Book } from 'lucide-react';
import BookCard from './BookCard';

const HomePage = ({ 
  books,
  user,  // Make sure user is received
  addToCart,
  updateCartQuantity,
  addToWishlist,
  removeFromWishlist,
  isInWishlist,
  getCartItemQuantity
}) => {
  
  // Debug logging
  console.log('HomePage - User:', user);
  console.log('HomePage - Books count:', books.length);
  
  return (
    <div className="container">
      <div className="page-header">
        <h2 className="page-title">Featured Books</h2>
        <p className="page-subtitle">Discover your next great read</p>
      </div>

      <div className="book-grid">
        {books.map((book) => (
          <BookCard 
            key={book.id} 
            book={book}
            user={user}  // Pass user to BookCard
            addToCart={addToCart}
            updateCartQuantity={updateCartQuantity}
            addToWishlist={addToWishlist}
            removeFromWishlist={removeFromWishlist}
            isInWishlist={isInWishlist}
            getCartItemQuantity={getCartItemQuantity}
          />
        ))}
      </div>

      {books.length === 0 && (
        <div className="empty-state">
          <Book className="empty-icon" size={64} />
          <h3 className="empty-title">No books found</h3>
          <p className="empty-description">Try adjusting your search terms</p>
        </div>
      )}
    </div>
  );
};

export default HomePage;