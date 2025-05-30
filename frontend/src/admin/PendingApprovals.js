import React from 'react';

const PendingApprovals = ({ pendingBooks, pendingReviews, onApproveBook, onApproveReview, loading }) => {
  return (
    <div className="pending-approvals">
      <h2>Pending Approvals</h2>
      
      {pendingBooks.length > 0 && (
        <div className="pending-books">
          <h3>Pending Books ({pendingBooks.length})</h3>
          <div className="books-grid">
            {pendingBooks.map(book => (
              <div key={book.id} className="book-card pending">
                <img src={book.imageUrl || '/default-book.jpg'} alt={book.title} />
                <div className="book-info">
                  <h3>{book.title}</h3>
                  <p>Author: {book.author}</p>
                  <p>Price: ${book.price}</p>
                  <p>Stock: {book.stockQuantity}</p>
                  <div className="admin-actions">
                    <button 
                      onClick={() => onApproveBook(book.id, true)}
                      className="approve-btn"
                    >
                      Approve
                    </button>
                    <button 
                      onClick={() => onApproveBook(book.id, false)}
                      className="reject-btn"
                    >
                      Reject
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {pendingReviews.length > 0 && (
        <div className="pending-reviews">
          <h3>Pending Reviews ({pendingReviews.length})</h3>
          <div className="reviews-list">
            {pendingReviews.map(review => (
              <div key={review.id} className="review-card pending">
                <div className="review-header">
                  <h4>{review.book?.title}</h4>
                  <div className="rating">
                    {'★'.repeat(review.rating)}{'☆'.repeat(5-review.rating)}
                  </div>
                </div>
                <p className="review-text">{review.comment}</p>
                <div className="review-meta">
                  <span>By: {review.user?.name}</span>
                </div>
                <div className="admin-actions">
                  <button 
                    onClick={() => onApproveReview(review.id, true)}
                    className="approve-btn"
                  >
                    Approve
                  </button>
                  <button 
                    onClick={() => onApproveReview(review.id, false)}
                    className="reject-btn"
                  >
                    Reject
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {pendingBooks.length === 0 && pendingReviews.length === 0 && !loading && (
        <div className="no-pending">
          <p>No pending approvals at this time.</p>
        </div>
      )}
    </div>
  );
};

export default PendingApprovals;
