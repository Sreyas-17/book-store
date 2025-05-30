import React, { useState } from 'react';
import { useVendor } from '../contexts/VendorContext';
import { useAuth } from '../contexts/AuthContext';
import './VendorDashboard.css';
import {
  Store,
  Book,
  ShoppingCart,
  DollarSign,
  Plus,
  Edit,
  Trash2,
  CheckCircle,
  Clock,
  XCircle,
  Upload
} from 'lucide-react';

const VendorDashboard = ({ setCurrentPage }) => {
  const {
    vendorProfile,
    vendorBooks,
    vendorOrders,
    dashboard,
    addBook,
    updateBook,
    deleteBook,
    updateOrderStatus,
    fetchVendorProfile, // Added this missing function
    loading
  } = useVendor();

  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('overview');
  const [showAddBookForm, setShowAddBookForm] = useState(false);
  const [editingBook, setEditingBook] = useState(null);
  const [bookForm, setBookForm] = useState({
    title: '',
    author: '',
    isbn: '',
    description: '',
    price: '',
    stockQuantity: '',
    imageUrl: '',
    categoryId: ''
  });

  if (!user || user.role !== 'VENDOR') {
    return (
      <div className="empty-state">
        <Store className="empty-icon" size={64} />
        <h2 className="empty-title">Access Denied</h2>
        <p className="empty-description">You need vendor privileges to access this page</p>
        <button onClick={() => setCurrentPage('home')} className="btn btn-primary">
          Go Home
        </button>
      </div>
    );
  }

  // Debug vendor profile data
  console.log('🔍 VendorProfile Debug:', vendorProfile);
  console.log('📊 All vendor profile fields:', vendorProfile ? Object.keys(vendorProfile) : 'No vendor profile');

  // Check different possible approval field names
  const isApproved = vendorProfile?.isApproved ||
                     vendorProfile?.is_approved ||
                     vendorProfile?.approved ||
                     vendorProfile?.status === 'APPROVED';

  console.log('✅ Approval Status Check:', {
    isApproved: vendorProfile?.isApproved,
    is_approved: vendorProfile?.is_approved,
    approved: vendorProfile?.approved,
    status: vendorProfile?.status,
    finalResult: isApproved
  });

  if (vendorProfile && !isApproved) {
    return (
      <div className="empty-state">
        <Clock className="empty-icon" size={64} />
        <h2 className="empty-title">Vendor Approval Pending</h2>
        <p className="empty-description">Your vendor account is pending admin approval. You'll be notified once approved.</p>

        {/* Debug info - remove after fixing */}
        <div style={{
          marginTop: '20px',
          padding: '16px',
          backgroundColor: '#fef2f2',
          borderRadius: '8px',
          fontSize: '14px',
          fontFamily: 'monospace'
        }}>
          <strong>Debug Info:</strong><br/>
          isApproved: {String(vendorProfile?.isApproved)}<br/>
          is_approved: {String(vendorProfile?.is_approved)}<br/>
          approved: {String(vendorProfile?.approved)}<br/>
          status: {String(vendorProfile?.status)}<br/>
          Profile ID: {vendorProfile?.id || vendorProfile?.vendorId}<br/>
        </div>

        <button onClick={() => setCurrentPage('home')} className="btn btn-primary">
          Go Home
        </button>
        <button
          onClick={() => {
            console.log('🔄 Refreshing vendor profile...');
            fetchVendorProfile();
          }}
          className="btn btn-secondary"
          style={{ marginLeft: '12px' }}
        >
          Refresh Status
        </button>
      </div>
    );
  }

  const handleAddBook = async (e) => {
    e.preventDefault();
    const result = await addBook({
      ...bookForm,
      price: parseFloat(bookForm.price),
      stockQuantity: parseInt(bookForm.stockQuantity),
      categoryId: bookForm.categoryId ? parseInt(bookForm.categoryId) : null
    });

    if (result.success) {
      alert('Book added successfully! It will be available after admin approval.');
      setShowAddBookForm(false);
      setBookForm({
        title: '', author: '', isbn: '', description: '',
        price: '', stockQuantity: '', imageUrl: '', categoryId: ''
      });
    } else {
      alert('Failed to add book: ' + result.message);
    }
  };

  const handleEditBook = (book) => {
    setEditingBook(book);
    setBookForm({
      title: book.title,
      author: book.author,
      isbn: book.isbn || '',
      description: book.description || '',
      price: book.price.toString(),
      stockQuantity: book.stockQuantity.toString(),
      imageUrl: book.imageUrl || '',
      categoryId: book.category?.id?.toString() || ''
    });
    setShowAddBookForm(true);
  };

  const handleUpdateBook = async (e) => {
    e.preventDefault();
    const result = await updateBook(editingBook.id, {
      ...bookForm,
      price: parseFloat(bookForm.price),
      stockQuantity: parseInt(bookForm.stockQuantity),
      categoryId: bookForm.categoryId ? parseInt(bookForm.categoryId) : null
    });

    if (result.success) {
      alert('Book updated successfully! Changes will be available after admin re-approval.');
      setShowAddBookForm(false);
      setEditingBook(null);
      setBookForm({
        title: '', author: '', isbn: '', description: '',
        price: '', stockQuantity: '', imageUrl: '', categoryId: ''
      });
    } else {
      alert('Failed to update book: ' + result.message);
    }
  };

  const handleDeleteBook = async (bookId, bookTitle) => {
    // FIXED: Using window.confirm explicitly to avoid ESLint warning
    if (window.confirm(`Are you sure you want to delete "${bookTitle}"?`)) {
      const result = await deleteBook(bookId);
      if (result.success) {
        alert('Book deleted successfully!');
      } else {
        alert('Failed to delete book: ' + result.message);
      }
    }
  };

  const handleOrderStatusUpdate = async (orderId, newStatus) => {
    const result = await updateOrderStatus(orderId, newStatus);
    if (result.success) {
      alert('Order status updated successfully!');
    } else {
      alert('Failed to update order status: ' + result.message);
    }
  };

  const getBookStatusBadge = (book) => {
    if (book.isApproved) {
      return (
        <span className="vendor-status-badge approved">
          <CheckCircle size={12} />
          Approved
        </span>
      );
    } else {
      return (
        <span className="vendor-status-badge pending">
          <Clock size={12} />
          Pending
        </span>
      );
    }
  };

  const StatCard = ({ icon: Icon, title, value, color }) => (
    <div className="vendor-stat-card">
      <div className="vendor-stat-content">
        <div className="vendor-stat-info">
          <p className="vendor-stat-title">{title}</p>
          <p className="vendor-stat-value" style={{ color }}>{value}</p>
        </div>
        <Icon size={32} style={{ color }} />
      </div>
    </div>
  );

  const renderOverview = () => (
    <div className="vendor-overview">
      <h3 className="vendor-section-title">Vendor Overview</h3>

      {dashboard && (
        <div className="vendor-stats-grid">
          <StatCard
            icon={Book}
            title="Total Books"
            value={dashboard.totalBooks || 0}
            color="#2563eb"
          />
          <StatCard
            icon={CheckCircle}
            title="Approved Books"
            value={dashboard.approvedBooks || 0}
            color="#059669"
          />
          <StatCard
            icon={Clock}
            title="Pending Books"
            value={dashboard.pendingBooks || 0}
            color="#d97706"
          />
          <StatCard
            icon={ShoppingCart}
            title="Total Orders"
            value={dashboard.totalOrders || 0}
            color="#7c3aed"
          />
        </div>
      )}

      <div className="vendor-info-grid">
        <div className="vendor-business-info">
          <h4 className="vendor-card-title">Business Information</h4>
          <div className="vendor-info-list">
            <div className="vendor-info-item">
              <label className="vendor-info-label">Business Name</label>
              <p className="vendor-info-value">{vendorProfile?.businessName}</p>
            </div>
            <div className="vendor-info-item">
              <label className="vendor-info-label">Business Email</label>
              <p className="vendor-info-value">{vendorProfile?.businessEmail}</p>
            </div>
            <div className="vendor-info-item">
              <label className="vendor-info-label">Business Phone</label>
              <p className="vendor-info-value">{vendorProfile?.businessPhone}</p>
            </div>
          </div>
        </div>

        <div className="vendor-revenue-card">
          <h4 className="vendor-card-title">Revenue Overview</h4>
          <div className="vendor-revenue-center">
            <p className="vendor-revenue-amount">
              ${dashboard?.totalRevenue || '0.00'}
            </p>
            <p className="vendor-revenue-label">Total Revenue</p>
          </div>
          <div className="vendor-revenue-actions">
            <button
              onClick={() => setActiveTab('books')}
              className="btn btn-primary vendor-manage-btn"
            >
              Manage Books
            </button>
          </div>
        </div>
      </div>
    </div>
  );

  const renderBooks = () => (
    <div className="vendor-books-section">
      <div className="vendor-section-header">
        <h3 className="vendor-section-title">
          My Books ({vendorBooks.length})
        </h3>
        <button
          onClick={() => {
            setEditingBook(null);
            setBookForm({
              title: '', author: '', isbn: '', description: '',
              price: '', stockQuantity: '', imageUrl: '', categoryId: ''
            });
            setShowAddBookForm(true);
          }}
          className="btn btn-primary vendor-add-btn"
        >
          <Plus size={16} />
          Add New Book
        </button>
      </div>

      {showAddBookForm && (
        <div className="vendor-book-form">
          <h4 className="vendor-form-title">
            {editingBook ? 'Edit Book' : 'Add New Book'}
          </h4>
          <form onSubmit={editingBook ? handleUpdateBook : handleAddBook}>
            <div className="vendor-form-row-2">
              <div className="form-group">
                <label className="form-label">Title *</label>
                <input
                  type="text"
                  value={bookForm.title}
                  onChange={(e) => setBookForm({...bookForm, title: e.target.value})}
                  className="form-input"
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">Author *</label>
                <input
                  type="text"
                  value={bookForm.author}
                  onChange={(e) => setBookForm({...bookForm, author: e.target.value})}
                  className="form-input"
                  required
                />
              </div>
            </div>

            <div className="vendor-form-row-3">
              <div className="form-group">
                <label className="form-label">ISBN</label>
                <input
                  type="text"
                  value={bookForm.isbn}
                  onChange={(e) => setBookForm({...bookForm, isbn: e.target.value})}
                  className="form-input"
                />
              </div>
              <div className="form-group">
                <label className="form-label">Price *</label>
                <input
                  type="number"
                  step="0.01"
                  min="0"
                  value={bookForm.price}
                  onChange={(e) => setBookForm({...bookForm, price: e.target.value})}
                  className="form-input"
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">Stock Quantity *</label>
                <input
                  type="number"
                  min="0"
                  value={bookForm.stockQuantity}
                  onChange={(e) => setBookForm({...bookForm, stockQuantity: e.target.value})}
                  className="form-input"
                  required
                />
              </div>
            </div>

            <div className="form-group vendor-form-full">
              <label className="form-label">Description</label>
              <textarea
                value={bookForm.description}
                onChange={(e) => setBookForm({...bookForm, description: e.target.value})}
                className="form-input vendor-textarea"
                rows="3"
              />
            </div>

            <div className="form-group vendor-form-full">
              <label className="form-label">Image URL</label>
              <input
                type="url"
                value={bookForm.imageUrl}
                onChange={(e) => setBookForm({...bookForm, imageUrl: e.target.value})}
                className="form-input"
                placeholder="https://example.com/book-cover.jpg"
              />
            </div>

            <div className="vendor-form-actions">
              <button
                type="submit"
                disabled={loading}
                className="btn btn-success"
              >
                {loading ? 'Saving...' : (editingBook ? 'Update Book' : 'Add Book')}
              </button>
              <button
                type="button"
                onClick={() => {
                  setShowAddBookForm(false);
                  setEditingBook(null);
                }}
                className="btn btn-secondary"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      {vendorBooks.length === 0 ? (
        <div className="empty-state">
          <Book className="empty-icon" size={64} />
          <h3 className="empty-title">No books yet</h3>
          <p className="empty-description">Add your first book to start selling</p>
        </div>
      ) : (
        <div className="vendor-books-list">
          {vendorBooks.map((book) => (
            <div key={book.id} className="vendor-book-item">
              <div className="vendor-book-content">
                <img
                  src={book.imageUrl || `https://via.placeholder.com/80x120/4f46e5/ffffff?text=${encodeURIComponent(book.title)}`}
                  alt={book.title}
                  className="vendor-book-image"
                />
                <div className="vendor-book-details">
                  <div className="vendor-book-header">
                    <h4 className="vendor-book-title">{book.title}</h4>
                    {getBookStatusBadge(book)}
                  </div>
                  <p className="vendor-book-author">by {book.author}</p>
                  <p className="vendor-book-stock">Stock: {book.stockQuantity}</p>
                  <p className="vendor-book-price">${book.price}</p>
                  <div className="vendor-book-actions">
                    <button
                      onClick={() => handleEditBook(book)}
                      className="btn btn-outline btn-sm vendor-edit-btn"
                    >
                      <Edit size={16} />
                      Edit
                    </button>
                    <button
                      onClick={() => handleDeleteBook(book.id, book.title)}
                      className="btn btn-danger btn-sm vendor-delete-btn"
                    >
                      <Trash2 size={16} />
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );

  const renderOrders = () => (
    <div className="vendor-orders-section">
      <h3 className="vendor-section-title">
        My Orders ({vendorOrders.length})
      </h3>

      {vendorOrders.length === 0 ? (
        <div className="empty-state">
          <ShoppingCart className="empty-icon" size={64} />
          <h3 className="empty-title">No orders yet</h3>
          <p className="empty-description">Orders for your books will appear here</p>
        </div>
      ) : (
        <div className="vendor-orders-list">
          {vendorOrders.map((order) => (
            <div key={order.id} className="vendor-order-item">
              <div className="vendor-order-header">
                <div className="vendor-order-info">
                  <h4 className="vendor-order-number">
                    Order #{order.orderNumber}
                  </h4>
                  <p className="vendor-order-date">
                    {new Date(order.createdAt).toLocaleDateString()}
                  </p>
                </div>
                <div className="vendor-order-controls">
                  <p className="vendor-order-total">
                    ${order.totalAmount}
                  </p>
                  <select
                    value={order.status}
                    onChange={(e) => handleOrderStatusUpdate(order.id, e.target.value)}
                    className="vendor-status-select"
                  >
                    <option value="PENDING">Pending</option>
                    <option value="CONFIRMED">Confirmed</option>
                    <option value="SHIPPED">Shipped</option>
                    <option value="DELIVERED">Delivered</option>
                    <option value="CANCELLED">Cancelled</option>
                  </select>
                </div>
              </div>

              {order.orderItems && (
                <div className="vendor-order-items">
                  <h5 className="vendor-order-items-title">Items:</h5>
                  {order.orderItems.map((item, index) => (
                    <div key={index} className="vendor-order-item-row">
                      <span className="vendor-order-item-name">
                        {item.book?.title} x {item.quantity}
                      </span>
                      <span className="vendor-order-item-price">
                        ${item.totalPrice}
                      </span>
                    </div>
                  ))}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );

  const TabButton = ({ id, label }) => (
    <button
      onClick={() => setActiveTab(id)}
      className={`vendor-tab-button ${activeTab === id ? 'active' : ''}`}
    >
      {label}
    </button>
  );

  return (
    <div className="vendor-dashboard">
      <div className="container">
        <div className="page-header">
          <h2 className="page-title">Vendor Dashboard</h2>
          <p className="page-subtitle">Manage your bookstore business</p>
        </div>

        <div className="vendor-tabs">
          <div className="vendor-tabs-container">
            <TabButton id="overview" label="Overview" />
            <TabButton id="books" label="My Books" />
            <TabButton id="orders" label="Orders" />
          </div>
        </div>

        {activeTab === 'overview' && renderOverview()}
        {activeTab === 'books' && renderBooks()}
        {activeTab === 'orders' && renderOrders()}
      </div>
    </div>
  );
};

export default VendorDashboard;