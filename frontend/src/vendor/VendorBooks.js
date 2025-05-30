import React from 'react';

const VendorBooks = ({ books, loading, onAddBook, onEditBook, onDeleteBook }) => {
  return (
    <div className="vendor-books">
      <div className="books-header">
        <h2>My Books</h2>
        <button className="add-book-btn" onClick={onAddBook}>
          Add New Book
        </button>
      </div>

      {loading && <div className="loading">Loading books...</div>}

      <div className="books-grid">
        {books.map(book => (
          <div key={book.id} className="book-card vendor">
            <img src={book.imageUrl || '/default-book.jpg'} alt={book.title} />
            <div className="book-info">
              <h3>{book.title}</h3>
              <p>Author: {book.author}</p>
              <p>Price: ${book.price}</p>
              <p>Stock: {book.stockQuantity}</p>
              <p>Rating: {book.averageRating ? book.averageRating.toFixed(1) : 'No ratings'}</p>
              <p>Status: <span className={`status ${book.approved ? 'approved' : 'pending'}`}>
                {book.approved ? 'Approved' : 'Pending Approval'}
              </span></p>
              <div className="vendor-actions">
                <button onClick={() => onEditBook(book)} className="edit-btn">
                  Edit
                </button>
                <button onClick={() => onDeleteBook(book.id)} className="delete-btn">
                  Delete
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {books.length === 0 && !loading && (
        <div className="no-books">
          <p>You haven't added any books yet.</p>
          <button className="add-first-book-btn" onClick={onAddBook}>
            Add Your First Book
          </button>
        </div>
      )}
    </div>
  );
};

export default VendorBooks;
