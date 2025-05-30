import React from 'react';

const ReviewManagement = ({ reviews, onApprove, loading }) => {
  return (
    <div className="reviews-management">
      <h2>All Reviews</h2>
      {loading && <div className="loading">Loading reviews...</div>}
      
      <div className="reviews-list">
        {reviews.map(review => (
          <div key={review.id} className="review-card admin">
            <div className="review-header">
              <h4>{review.book?.title}</h4>
              <div className="rating">
                {'★'.repeat(review.rating)}{'☆'.repeat(5-review.rating)}
              </div>
            </div>
            <p className="review-text">{review.comment}</p>
            <div className="review-meta">
              <span>By: {review.user?.name}</span>
              <span>Status: <span className={`status ${review.approved ? 'approved' : 'pending'}`}>
                {review.approved ? 'Approved' : 'Pending'}
              </span></span>
            </div>
            <div className="admin-actions">
              <button 
                onClick={() => onApprove(review.id, true)}
                className="approve-btn"
                disabled={review.approved}
              >
                Approve
              </button>
              <button 
                onClick={() => onApprove(review.id, false)}
                className="reject-btn"
              >
                Reject
              </button>
            </div>
          </div>
        ))}
      </div>
      
      {reviews.length === 0 && !loading && (
        <div className="no-data">No reviews found.</div>
      )}
    </div>
  );
};

export default ReviewManagement;
