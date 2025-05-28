import React from 'react';
import { MapPin } from 'lucide-react';

const OrdersPage = ({ user, orders, setCurrentPage }) => {
  if (!user) {
    return (
      <div className="empty-state">
        <MapPin className="empty-icon" size={64} />
        <h2 className="empty-title">Please sign in to view your orders</h2>
        <button
          onClick={() => setCurrentPage('login')}
          className="btn btn-primary"
        >
          Sign In
        </button>
      </div>
    );
  }

  if (orders.length === 0) {
    return (
      <div className="empty-state">
        <MapPin className="empty-icon" size={64} />
        <h2 className="empty-title">No orders yet</h2>
        <p className="empty-description">Your order history will appear here</p>
        <button
          onClick={() => setCurrentPage('home')}
          className="btn btn-primary"
        >
          Start Shopping
        </button>
      </div>
    );
  }

  const getStatusStyle = (status) => {
    const baseStyle = {
      display: 'inline-block',
      padding: '4px 12px',
      borderRadius: '20px',
      fontSize: '12px',
      fontWeight: '500'
    };

    switch(status) {
      case 'PENDING':
        return {...baseStyle, backgroundColor: '#fef3c7', color: '#d97706'};
      case 'CONFIRMED':
        return {...baseStyle, backgroundColor: '#dbeafe', color: '#2563eb'};
      case 'SHIPPED':
        return {...baseStyle, backgroundColor: '#e9d5ff', color: '#7c3aed'};
      case 'DELIVERED':
        return {...baseStyle, backgroundColor: '#d1fae5', color: '#059669'};
      default:
        return {...baseStyle, backgroundColor: '#fee2e2', color: '#dc2626'};
    }
  };

  return (
    <div className="container">
      <div className="page-header">
        <h2 className="page-title">My Orders</h2>
      </div>
      
      <div className="space-y-6">
        {orders.map((order) => (
          <div key={order.id} className="card" style={{padding: '24px'}}>
            <div className="flex justify-between items-start mb-4">
              <div>
                <h3 style={{fontSize: '18px', fontWeight: '600', color: '#1f2937', marginBottom: '4px'}}>
                  Order #{order.orderNumber}
                </h3>
                <p style={{color: '#6b7280', fontSize: '14px'}}>
                  Placed on {new Date(order.createdAt).toLocaleDateString()}
                </p>
              </div>
              <div style={{textAlign: 'right'}}>
                <p style={{fontSize: '24px', fontWeight: 'bold', color: '#2563eb', marginBottom: '8px'}}>
                  {order.totalAmount}
                </p>
                <span style={getStatusStyle(order.status)}>
                  {order.status}
                </span>
              </div>
            </div>
            
            {order.orderItems && (
              <div style={{borderTop: '1px solid #e5e7eb', paddingTop: '16px'}}>
                <h4 style={{fontWeight: '500', color: '#1f2937', marginBottom: '8px'}}>Items:</h4>
                <div className="space-y-4">
                  {order.orderItems.map((item, index) => (
                    <div key={index} style={{display: 'flex', justifyContent: 'space-between', fontSize: '14px'}}>
                      <span>{item.book?.title || 'Book'} x {item.quantity}</span>
                      <span>{item.totalPrice}</span>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default OrdersPage;