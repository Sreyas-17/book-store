import React, { useEffect } from 'react';
import { useBooks } from '../hooks/useBooks';
import { useAuth } from '../contexts/AuthContext';
import BookCard from './BookCard';
import '../index.css';

const HomePage = () => {
  const { books = [], loading, error, searchBooks } = useBooks(); // Default to empty array
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    // Initial load of books if needed
    if (!books || books.length === 0) {
      // You might want to fetch all books initially
      // searchBooks(''); // or call a fetchAllBooks function
    }
  }, []);

  // Defensive check - ensure books is always an array
  const safeBooks = Array.isArray(books) ? books : [];

  if (loading) {
    return (
      <div className="homepage">
        <div className="loading-container">
          <div className="loading-spinner">Loading books...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="homepage">
        <div className="error-container">
          <p className="error-message">Error: {error}</p>
          <button onClick={() => window.location.reload()}>
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="homepage">
      <div className="hero-section">
        <h1>Welcome to BookStore</h1>
        <p>Discover your next favorite book</p>
      </div>

      <div className="books-section">
        <div className="books-header">
          <h2>Featured Books</h2>
          {safeBooks.length > 0 && (
            <p className="books-count">
              {safeBooks.length} book{safeBooks.length !== 1 ? 's' : ''} found
            </p>
          )}
        </div>

        {safeBooks.length === 0 ? (
          <div className="no-books">
            <p>No books available at the moment.</p>
            <p>Try searching for specific books using the search bar above.</p>
          </div>
        ) : (
          <div className="books-grid">
            {safeBooks.map((book) => (
              <BookCard 
                key={book.id} 
                book={book} 
                showActions={isAuthenticated}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default HomePage;
