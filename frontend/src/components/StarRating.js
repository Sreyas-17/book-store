import React, { useState } from 'react';
import { Star } from 'lucide-react';

const StarRating = ({ 
  bookId, 
  currentRating = 0, 
  totalRatings = 0, 
  onRate, 
  readonly = false,
  size = 16 
}) => {
  const [hoverRating, setHoverRating] = useState(0);
  const [userRating, setUserRating] = useState(0);

  const handleStarClick = async (rating) => {
    if (readonly) return;
    
    setUserRating(rating);
    if (onRate) {
      await onRate(bookId, rating);
    }
  };

  const handleStarHover = (rating) => {
    if (readonly) return;
    setHoverRating(rating);
  };

  const handleMouseLeave = () => {
    if (readonly) return;
    setHoverRating(0);
  };

  const displayRating = userRating || hoverRating || currentRating;

  return (
    <div className="star-rating" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
      {/* Stars */}
      <div 
        className="stars" 
        style={{ display: 'flex', gap: '2px' }}
        onMouseLeave={handleMouseLeave}
      >
        {[1, 2, 3, 4, 5].map((star) => (
          <Star
            key={star}
            size={size}
            className={`star ${
              star <= displayRating ? 'star-filled' : 'star-empty'
            } ${readonly ? '' : 'star-interactive'}`}
            style={{
              color: star <= displayRating ? '#fbbf24' : '#d1d5db',
              cursor: readonly ? 'default' : 'pointer',
              transition: 'color 0.2s ease',
            }}
            fill={star <= displayRating ? 'currentColor' : 'none'}
            onClick={() => handleStarClick(star)}
            onMouseEnter={() => handleStarHover(star)}
          />
        ))}
      </div>

      {/* Rating Info */}
      <span className="rating-info" style={{ fontSize: '12px', color: '#6b7280' }}>
        {readonly ? (
          `(${totalRatings} ${totalRatings === 1 ? 'review' : 'reviews'})`
        ) : (
          userRating > 0 ? `You rated: ${userRating} stars` : 'Click to rate'
        )}
      </span>
    </div>
  );
};

export default StarRating;