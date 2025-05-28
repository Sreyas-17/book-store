import React from 'react';
import { ShoppingCart, Plus, Minus } from 'lucide-react';

const CartPage = ({ 
  user, 
  cart, 
  setCurrentPage, 
  updateCartQuantity, 
  removeFromCart, 
  getCartTotal 
}) => {
  if (!user) {
    return (
      <div className="empty-state">
        <ShoppingCart className="empty-icon" size={64} />
        <h2 className="empty-title">Please sign in to view your cart</h2>
        <button
          onClick={() => setCurrentPage('login')}
          className="btn btn-primary"
        >
          Sign In
        </button>
      </div>
    );
  }

  if (cart.length === 0) {
    return (
      <div className="empty-state">
        <ShoppingCart className="empty-icon" size={64} />
        <h2 className="empty-title">Your cart is empty</h2>
        <p className="empty-description">Add some books to get started!</p>
        <button
          onClick={() => setCurrentPage('home')}
          className="btn btn-primary"
        >
          Continue Shopping
        </button>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="page-header">
        <h2 className="page-title">Shopping Cart</h2>
      </div>
      
      <div className="cart-layout">
        <div className="cart-items">
          {cart.map((item) => (
            <div key={item.id} className="cart-item">
              <div className="cart-item-content">
                <img
                  src={item.book.imageUrl || 'https://via.placeholder.com/80x96?text=Book'}
                  alt={item.book.title}
                  className="cart-item-image"
                />
                
                <div className="cart-item-details">
                  <h3 className="cart-item-title">
                    {item.book.title}
                  </h3>
                  <p className="cart-item-author">by {item.book.author}</p>
                  <p className="cart-item-price">
                    ${item.book.price}
                  </p>
                </div>

                <div className="cart-item-controls">
                  <div className="quantity-controls">
                    <button
                      onClick={() => updateCartQuantity(item.book.id, item.quantity - 1)}
                      className="quantity-btn"
                    >
                      <Minus size={16} />
                    </button>
                    <span className="quantity-display">{item.quantity}</span>
                    <button
                      onClick={() => updateCartQuantity(item.book.id, item.quantity + 1)}
                      className="quantity-btn"
                    >
                      <Plus size={16} />
                    </button>
                  </div>

                  <button
                    onClick={() => removeFromCart(item.book.id)}
                    className="remove-btn"
                  >
                    Remove
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="order-summary">
          <h3 className="summary-title">Order Summary</h3>
          
          <div className="summary-line">
            <span>Subtotal</span>
            <span>{getCartTotal()}</span>
          </div>
          <div className="summary-line">
            <span>Shipping</span>
            <span>Free</span>
          </div>
          <div className="summary-total">
            <span>Total</span>
            <span>{getCartTotal()}</span>
          </div>

          <button
            onClick={() => setCurrentPage('checkout')}
            className="checkout-btn"
          >
            Proceed to Checkout
          </button>
        </div>
      </div>
    </div>
  );
};

export default CartPage;