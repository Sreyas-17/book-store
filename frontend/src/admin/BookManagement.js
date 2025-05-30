import React from 'react';

const BookManagement = ({ books, onApprove, loading }) => {
  return (
    <div className="books-management">
      <h2>All Books</h2>
      {loading && <div className="loading">Loading books...</div>}
      
      <div className="books-grid">
        {books.map(book => (
          <div key={book.id} className="book-card admin">
            <img src={book.imageUrl || '/default-book.jpg'} alt={book.title} />
            <div className="book-info">
              <h3>{book.title}</h3>
              <p>Author: {book.author}</p>
              <p>Price: ${book.price}</p>
              <p>Stock: {book.stockQuantity}</p>
              <p>Status: <span className={`status ${book.approved ? 'approved' : 'pending'}`}>
                {book.approved ? 'Approved' : 'Pending'}
              </span></p>
              <div className="admin-actions">
                <button 
                  onClick={() => onApprove(book.id, true)}
                  className="approve-btn"
                  disabled={book.approved}
                >
                  Approve
                </button>
                <button 
                  onClick={() => onApprove(book.id, false)}
                  className="reject-btn"
                >
                  Reject
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
      
      {books.length === 0 && !loading && (
        <div className="no-data">No books found.</div>
      )}
    </div>
  );
};

export default BookManagement;
