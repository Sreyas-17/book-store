import React from 'react';
import { Book } from 'lucide-react';
import BookCard from './BookCard';

const HomePage = ({ 
  books,
  user,
  addToCart,
  updateCartQuantity,
  addToWishlist,
  removeFromWishlist,
  isInWishlist,
  getCartItemQuantity,
  rateBook 
}) => {
  

  console.log('HomePage - User:', user);
  console.log('HomePage - Books count:', books.length);
  console.log('HomePage - rateBook function:', typeof rateBook);
  
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
            user={user}
            addToCart={addToCart}
            updateCartQuantity={updateCartQuantity}
            addToWishlist={addToWishlist}
            removeFromWishlist={removeFromWishlist}
            isInWishlist={isInWishlist}
            getCartItemQuantity={getCartItemQuantity}
            rateBook={rateBook} 
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