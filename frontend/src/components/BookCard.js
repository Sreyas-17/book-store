import React from "react";
import { Heart, Plus, Minus } from "lucide-react";
import StarRating from './StarRating'; 

const BookCard = ({
  book,
  user,
  addToCart,
  updateCartQuantity,
  addToWishlist,
  removeFromWishlist,
  isInWishlist,
  getCartItemQuantity,
  rateBook, 
}) => {
 
  console.log(
    "BookCard - Book ID:",
    book.id,
    "User:",
    user?.email || "No user"
  );

  if (!addToCart || !isInWishlist || !getCartItemQuantity) {
    console.error("BookCard: Required functions not passed as props");
    return <div>Error: Missing required functions</div>;
  }

  const isWishlisted = isInWishlist(book.id);
  const cartQuantity = getCartItemQuantity(book.id);

  console.log(
    "BookCard - Wishlisted:",
    isWishlisted,
    "Cart Quantity:",
    cartQuantity
  );

  const handleAddToCart = async () => {
    console.log("Add to cart clicked for book:", book.id);

    if (!user || !user.id) {
      console.log("No user logged in, cannot add to cart");
      alert("Please login to add items to cart");
      return;
    }

    try {
      const result = await addToCart(book.id, 1);
      console.log("Add to cart result:", result);

      if (result && !result.success) {
        alert("Failed to add to cart: " + result.message);
      }
    } catch (error) {
      console.error("Error adding to cart:", error);
      alert("Error adding to cart");
    }
  };

  const handleUpdateQuantity = async (newQuantity) => {
    console.log("Update quantity to:", newQuantity, "for book:", book.id);

    if (!user || !user.id) {
      console.log("No user logged in");
      return;
    }

    try {
      await updateCartQuantity(book.id, newQuantity);
    } catch (error) {
      console.error("Error updating quantity:", error);
    }
  };

  const handleWishlistToggle = async () => {
    console.log(
      "Wishlist toggle for book:",
      book.id,
      "Currently wishlisted:",
      isWishlisted
    );

    if (!user || !user.id) {
      console.log("No user logged in, cannot modify wishlist");
      alert("Please login to manage wishlist");
      return;
    }

    try {
      if (isWishlisted) {
        const result = await removeFromWishlist(book.id);
        console.log("Remove from wishlist result:", result);
      } else {
        const result = await addToWishlist(book.id);
        console.log("Add to wishlist result:", result);
      }
    } catch (error) {
      console.error("Error updating wishlist:", error);
      alert("Error updating wishlist");
    }
  };

  const handleRating = async (bookId, rating) => {
    if (!user || !user.id) {
      alert('Please login to rate books');
      return;
    }

    if (rateBook) {
      try {
        console.log(' Rating book:', bookId, 'with', rating, 'stars');
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
    <div className="book-card">
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
        className="book-image"
        onError={(e) => {
          e.target.src = `https://via.placeholder.com/300x400/6366f1/ffffff?text=${encodeURIComponent(
            book.title
          )}`;
        }}
      />

      <div className="book-content">
        <h3 className="book-title">{book.title}</h3>
        <p className="book-author">by {book.author}</p>

        <div className="book-rating">
          <StarRating
            bookId={book.id}
            currentRating={book.ratingAvg || 0}
            totalRatings={book.totalRatings || 0}
            onRate={handleRating}
            readonly={!user}
            size={16}
          />
        </div>

        <div className="book-price-row">
          <span className="book-price">{book.price}</span>
          <span className="book-stock">Stock: {book.stockQuantity}</span>
        </div>

        <div className="book-actions">
          {cartQuantity > 0 ? (
            <div className="quantity-controls">
              <button
                onClick={() => handleUpdateQuantity(cartQuantity - 1)}
                className="quantity-btn"
              >
                <Minus size={16} />
              </button>
              <span className="quantity-display">{cartQuantity}</span>
              <button
                onClick={() => handleUpdateQuantity(cartQuantity + 1)}
                className="quantity-btn"
              >
                <Plus size={16} />
              </button>
            </div>
          ) : (
            <button onClick={handleAddToCart} className="add-to-cart-btn">
              Add to Cart
            </button>
          )}

          <button
            onClick={handleWishlistToggle}
            className={`wishlist-btn ${
              isWishlisted ? "wishlist-btn-active" : "wishlist-btn-inactive"
            }`}
          >
            <Heart size={20} fill={isWishlisted ? "currentColor" : "none"} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default BookCard;