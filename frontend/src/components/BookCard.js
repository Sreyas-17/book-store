import React from "react";
import { Heart, Plus, Minus, ShoppingCart } from "lucide-react";
import StarRating from './StarRating';
import { useAuth } from '../contexts/AuthContext';
import { useCart } from '../contexts/CartContext';
import { useWishlist } from '../contexts/WishlistContext';

const BookCard = ({ book, rateBook }) => {
  const { user } = useAuth();
  const { addToCart, updateQuantity, cartItems = [] } = useCart();
  const { addToWishlist, removeFromWishlist, wishlistItems = [] } = useWishlist();

  // Get cart quantity for this book
  const cartItem = cartItems.find(item => item.book?.id === book.id);
  const cartQuantity = cartItem?.quantity || 0;

  // Check if book is in wishlist
  const isInWishlist = wishlistItems.some(item => item.book?.id === book.id);

  const handleAddToCart = async () => {
    if (!user?.id) {
      alert("Please login to add items to cart");
      return;
    }

    try {
      const result = await addToCart(book.id, 1);
      if (result && !result.success) {
        alert("Failed to add to cart: " + result.message);
      }
    } catch (error) {
      console.error("Error adding to cart:", error);
      alert("Error adding to cart");
    }
  };

  const handleUpdateQuantity = async (newQuantity) => {
    if (!user?.id) return;

    try {
      await updateQuantity(book.id, newQuantity);
    } catch (error) {
      console.error("Error updating quantity:", error);
    }
  };

  const handleWishlistToggle = async () => {
    if (!user?.id) {
      alert("Please login to manage wishlist");
      return;
    }

    try {
      if (isInWishlist) {
        await removeFromWishlist(book.id);
      } else {
        await addToWishlist(book.id);
      }
    } catch (error) {
      console.error("Error updating wishlist:", error);
      alert("Error updating wishlist");
    }
  };

  const handleRating = async (bookId, rating) => {
    if (!user?.id) {
      alert('Please login to rate books');
      return;
    }

    if (rateBook) {
      try {
        const result = await rateBook(bookId, rating);
        if (result && result.success) {
          alert(`Thank you for rating this book ${rating} stars!`);
        } else {
          alert('Failed to rate book: ' + (result?.message || 'Unknown error'));
        }
      } catch (error) {
        console.error('Error rating book:', error);
        alert('Error submitting rating');
      }
    }
  };

  return (
    <div className="book-card card-hover group">
      {/* Book Image */}
      <div className="relative overflow-hidden">
        <img
          src={
            book.imageUrl &&
            book.imageUrl !== "https://example.com/gatsby.jpg" &&
            book.imageUrl !== "https://example.com/mockingbird.jpg"
              ? book.imageUrl
              : `https://via.placeholder.com/300x400/4f46e5/ffffff?text=${encodeURIComponent(
                  book.title
                )}`
          }
          alt={book.title}
          className="w-full h-48 object-cover group-hover:scale-105 transition-transform duration-300"
          onError={(e) => {
            e.target.src = `https://via.placeholder.com/300x400/6366f1/ffffff?text=${encodeURIComponent(
              book.title
            )}`;
          }}
        />
        
        {/* Wishlist Button Overlay */}
        <button
          onClick={handleWishlistToggle}
          className={`absolute top-3 right-3 p-2 rounded-full transition-all duration-200 ${
            isInWishlist 
              ? 'bg-red-500 text-white shadow-lg' 
              : 'bg-white text-gray-600 hover:bg-red-50 hover:text-red-500 shadow-md'
          }`}
        >
          <Heart size={16} fill={isInWishlist ? "currentColor" : "none"} />
        </button>
      </div>

      {/* Book Content */}
      <div className="card-body">
        <h3 className="book-title">{book.title}</h3>
        <p className="book-author">by {book.author}</p>

        {/* Rating */}
        <div className="mb-3">
          <StarRating
            bookId={book.id}
            currentRating={book.ratingAvg || 0}
            totalRatings={book.totalRatings || 0}
            onRate={handleRating}
            readonly={!user}
            size={16}
          />
        </div>

        {/* Price and Stock */}
        <div className="flex justify-between items-center mb-4">
          <span className="book-price">${book.price}</span>
          <span className="text-sm text-gray-500">Stock: {book.stockQuantity}</span>
        </div>

        {/* Actions */}
        <div className="flex items-center gap-2">
          {cartQuantity > 0 ? (
            <div className="flex items-center bg-gray-100 rounded-lg p-1 flex-1">
              <button
                onClick={() => handleUpdateQuantity(cartQuantity - 1)}
                className="p-1 hover:bg-gray-200 rounded transition-colors duration-200"
              >
                <Minus size={16} />
              </button>
              <span className="px-3 py-1 text-sm font-medium">{cartQuantity}</span>
              <button
                onClick={() => handleUpdateQuantity(cartQuantity + 1)}
                className="p-1 hover:bg-gray-200 rounded transition-colors duration-200"
              >
                <Plus size={16} />
              </button>
            </div>
          ) : (
            <button 
              onClick={handleAddToCart} 
              className="btn btn-primary flex-1 flex items-center justify-center gap-2"
            >
              <ShoppingCart size={16} />
              Add to Cart
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default BookCard;
