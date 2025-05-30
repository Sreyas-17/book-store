// src/components/AdminDashboard.js - Complete fixed version with CSS import
import React, { useState, useEffect } from 'react';
import { useAdmin } from '../contexts/AdminContext';
import { useAuth } from '../contexts/AuthContext';
import './AdminDashboard.css'; // ← IMPORTANT: CSS import added

import {
  Users,
  Book,
  Store,
  ShoppingCart,
  DollarSign,
  CheckCircle,
  XCircle,
  Clock,
  TrendingUp,
  Search,
  Filter,
  Eye
} from 'lucide-react';

const AdminDashboard = ({ setCurrentPage }) => {
  const {
    dashboard,
    pendingBooks,
    pendingVendors,
    allUsers,
    allOrders,
    approveBook,
    disapproveBook,
    approveVendor,
    disapproveVendor,
    updateUserRole,
    fetchAllUsers,
    fetchAllOrders,
    loading
  } = useAdmin();

  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('overview');
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('all');

  // New state for filtered data
  const [filteredBooks, setFilteredBooks] = useState([]);
  const [filteredVendors, setFilteredVendors] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);

  // Update filtered data when original data changes
  useEffect(() => {
    applyFilters();
  }, [pendingBooks, pendingVendors, allUsers, searchTerm, filterStatus]);

  const applyFilters = () => {
    // Filter books
    let booksToShow = pendingBooks;
    if (searchTerm) {
      booksToShow = booksToShow.filter(book =>
        book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        book.author.toLowerCase().includes(searchTerm.toLowerCase()) ||
        book.vendor?.businessName?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    if (filterStatus !== 'all') {
      booksToShow = booksToShow.filter(book => {
        switch (filterStatus) {
          case 'approved': return book.isApproved === true;
          case 'pending': return book.isApproved === false;
          case 'active': return book.isApproved === true;
          default: return true;
        }
      });
    }
    setFilteredBooks(booksToShow);

    // Filter vendors
    let vendorsToShow = pendingVendors;
    if (searchTerm) {
      vendorsToShow = vendorsToShow.filter(vendor =>
        vendor.businessName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        vendor.businessEmail?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        vendor.user?.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        vendor.user?.lastName?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    if (filterStatus !== 'all') {
      vendorsToShow = vendorsToShow.filter(vendor => {
        switch (filterStatus) {
          case 'approved': return vendor.isApproved === true;
          case 'pending': return vendor.isApproved === false;
          case 'active': return vendor.isApproved === true;
          default: return true;
        }
      });
    }
    setFilteredVendors(vendorsToShow);

    // Filter users
    let usersToShow = allUsers;
    if (searchTerm) {
      usersToShow = usersToShow.filter(user =>
        user.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.lastName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.email?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    if (filterStatus !== 'all') {
      usersToShow = usersToShow.filter(user => {
        switch (filterStatus) {
          case 'active': return user.isActive === true;
          case 'pending': return user.isVerified === false;
          case 'approved': return user.isVerified === true;
          default: return true;
        }
      });
    }
    setFilteredUsers(usersToShow);
  };

  const handleSearch = (term) => {
    setSearchTerm(term);
  };

  const handleFilter = (status) => {
    setFilterStatus(status);
  };

  const resetFilters = () => {
    setSearchTerm('');
    setFilterStatus('all');
  };

  // Load data when switching tabs - FIXED: Moved useEffect before early return
  useEffect(() => {
    if (activeTab === 'users' && allUsers.length === 0) {
      fetchAllUsers();
    }
    if (activeTab === 'orders' && allOrders.length === 0) {
      fetchAllOrders();
    }
  }, [activeTab, allUsers.length, allOrders.length, fetchAllUsers, fetchAllOrders]);

  // Access control - FIXED: Early return moved after all hooks
  if (!user || user.role !== 'ADMIN') {
    return (
      <div className="empty-state">
        <Users className="empty-icon" size={64} />
        <h2 className="empty-title">Access Denied</h2>
        <p className="empty-description">You need admin privileges to access this page</p>
        <button onClick={() => setCurrentPage('home')} className="btn btn-primary">
          Go Home
        </button>
      </div>
    );
  }

  // Handler functions
  const handleApproveBook = async (bookId) => {
    const confirmed = window.confirm('Are you sure you want to approve this book?');
    if (confirmed) {
      const result = await approveBook(bookId);
      if (result.success) {
        alert('Book approved successfully!');
      } else {
        alert('Failed to approve book: ' + result.message);
      }
    }
  };

  const handleDisapproveBook = async (bookId) => {
    const confirmed = window.confirm('Are you sure you want to disapprove this book?');
    if (confirmed) {
      const result = await disapproveBook(bookId);
      if (result.success) {
        alert('Book disapproved successfully!');
      } else {
        alert('Failed to disapprove book: ' + result.message);
      }
    }
  };

  const handleApproveVendor = async (vendorId) => {
    const confirmed = window.confirm('Are you sure you want to approve this vendor?');
    if (confirmed) {
      const result = await approveVendor(vendorId);
      if (result.success) {
        alert('Vendor approved successfully!');
      } else {
        alert('Failed to approve vendor: ' + result.message);
      }
    }
  };

  const handleDisapproveVendor = async (vendorId) => {
    const confirmed = window.confirm('Are you sure you want to disapprove this vendor?');
    if (confirmed) {
      const result = await disapproveVendor(vendorId);
      if (result.success) {
        alert('Vendor disapproved successfully!');
      } else {
        alert('Failed to disapprove vendor: ' + result.message);
      }
    }
  };

  const handleUpdateUserRole = async (userId, newRole) => {
    const confirmed = window.confirm(`Are you sure you want to change this user's role to ${newRole}?`);
    if (confirmed) {
      const result = await updateUserRole(userId, newRole);
      if (result.success) {
        alert('User role updated successfully!');
      } else {
        alert('Failed to update user role: ' + result.message);
      }
    }
  };

  // Utility components
  const StatCard = ({ icon: Icon, title, value, color, subtitle, onClick, trend }) => (
    <div className={`admin-stat-card ${onClick ? 'clickable' : ''}`} onClick={onClick}>
      <div className="admin-stat-content">
        <div className="admin-stat-info">
          <p className="admin-stat-title">{title}</p>
          <p className="admin-stat-value" style={{ color }}>{value}</p>
          {subtitle && <p className="admin-stat-subtitle">{subtitle}</p>}
          {trend && (
            <div className={`admin-stat-trend ${trend > 0 ? 'positive' : 'negative'}`}>
              <TrendingUp size={12} />
              {trend > 0 ? '+' : ''}{trend}% from last month
            </div>
          )}
        </div>
        <div className="admin-stat-icon" style={{ backgroundColor: `${color}15` }}>
          <Icon size={24} style={{ color }} />
        </div>
      </div>
    </div>
  );

  const TabButton = ({ id, label, count = null, icon: Icon }) => (
    <button
      onClick={() => setActiveTab(id)}
      className={`admin-tab-button ${activeTab === id ? 'active' : ''}`}
    >
      {Icon && <Icon size={16} />}
      {label}
      {count !== null && count > 0 && (
        <span className="admin-tab-badge">{count}</span>
      )}
    </button>
  );

  const SearchAndFilter = ({ onSearch, onFilter, placeholder = "Search..." }) => (
    <div className="admin-search-filter">
      <div className="admin-search-container">
        <Search size={18} className="admin-search-icon" />
        <input
          type="text"
          placeholder={placeholder}
          value={searchTerm}
          onChange={(e) => {
            setSearchTerm(e.target.value);
            onSearch(e.target.value);
          }}
          className="admin-search-input"
        />
      </div>

      <div className="admin-filter-container">
        <Filter size={16} />
        <select
          value={filterStatus}
          onChange={(e) => {
            setFilterStatus(e.target.value);
            onFilter(e.target.value);
          }}
          className="admin-filter-select"
        >
          <option value="all">All Status</option>
          <option value="active">Active</option>
          <option value="pending">Pending</option>
          <option value="approved">Approved</option>
        </select>
      </div>

      {(searchTerm || filterStatus !== 'all') && (
        <button
          onClick={resetFilters}
          className="btn btn-secondary btn-sm"
          style={{ marginLeft: '12px' }}
        >
          Clear Filters
        </button>
      )}
    </div>
  );

  // Tab content renderers
  const renderOverview = () => (
    <div className="admin-overview">
      <div className="admin-overview-header">
        <h3 className="admin-overview-title">Admin Dashboard Overview</h3>
        <p className="admin-overview-subtitle">Monitor your bookstore's performance and manage operations</p>
      </div>

      {dashboard && (
        <div className="admin-stats-grid">
          <StatCard
            icon={Users}
            title="Total Users"
            value={dashboard.users?.total || 0}
            color="#2563eb"
            subtitle="Registered customers"
            trend={12}
            onClick={() => {
              setActiveTab('users');
              fetchAllUsers();
            }}
          />
          <StatCard
            icon={Book}
            title="Total Books"
            value={dashboard.books?.total || 0}
            color="#059669"
            subtitle={`${dashboard.books?.approved || 0} approved`}
            trend={8}
          />
          <StatCard
            icon={Store}
            title="Total Vendors"
            value={dashboard.vendors?.total || 0}
            color="#7c3aed"
            subtitle={`${dashboard.vendors?.approved || 0} active`}
            trend={5}
          />
          <StatCard
            icon={ShoppingCart}
            title="Total Orders"
            value={dashboard.orders?.total || 0}
            color="#dc2626"
            subtitle="All time orders"
            trend={15}
            onClick={() => {
              setActiveTab('orders');
              fetchAllOrders();
            }}
          />
        </div>
      )}

      <div className="admin-quick-stats">
        {/* Pending Approvals */}
        <div className="admin-card">
          <h4 className="admin-card-title">
            <Clock size={20} />
            Pending Approvals
          </h4>
          <div className="admin-pending-list">
            <div className="admin-pending-item books">
              <div className="admin-pending-info">
                <Book size={16} />
                <span>Books</span>
              </div>
              <span className="admin-pending-badge books">
                {dashboard?.books?.pending || 0}
              </span>
            </div>
            <div className="admin-pending-item vendors">
              <div className="admin-pending-info">
                <Store size={16} />
                <span>Vendors</span>
              </div>
              <span className="admin-pending-badge vendors">
                {dashboard?.vendors?.pending || 0}
              </span>
            </div>
          </div>
          <div className="admin-pending-actions">
            <button
              onClick={() => setActiveTab('books')}
              className="btn btn-warning btn-sm"
            >
              Review Books
            </button>
            <button
              onClick={() => setActiveTab('vendors')}
              className="btn btn-purple btn-sm"
            >
              Review Vendors
            </button>
          </div>
        </div>

        {/* Revenue Overview */}
        <div className="admin-card">
          <h4 className="admin-card-title">
            <DollarSign size={20} />
            Revenue Overview
          </h4>
          <div className="admin-revenue-center">
            <p className="admin-revenue-amount">
              ${dashboard?.orders?.totalRevenue || '0.00'}
            </p>
            <p className="admin-revenue-label">Total Revenue</p>
          </div>
          <div className="admin-revenue-grid">
            <div className="admin-revenue-metric">
              <p className="admin-revenue-metric-value" style={{ color: '#2563eb' }}>
                {dashboard?.orders?.total || 0}
              </p>
              <p className="admin-revenue-metric-label">Total Orders</p>
            </div>
            <div className="admin-revenue-metric">
              <p className="admin-revenue-metric-value" style={{ color: '#7c3aed' }}>
                ${((dashboard?.orders?.totalRevenue || 0) / Math.max(dashboard?.orders?.total || 1, 1)).toFixed(2)}
              </p>
              <p className="admin-revenue-metric-label">Avg Order Value</p>
            </div>
          </div>
        </div>

        {/* Recent Activity */}
        <div className="admin-card">
          <h4 className="admin-card-title">Recent Activity</h4>
          <div className="admin-activity-list">
            <div className="admin-activity-item">
              <div className="admin-activity-dot green"></div>
              <span>New vendor registration: Tech Books Inc.</span>
            </div>
            <div className="admin-activity-item">
              <div className="admin-activity-dot yellow"></div>
              <span>Book pending approval: "React Mastery"</span>
            </div>
            <div className="admin-activity-item">
              <div className="admin-activity-dot blue"></div>
              <span>New user registration: john@example.com</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );

  const renderPendingBooks = () => (
    <div className="admin-section">
      <div className="admin-section-header">
        <div>
          <h3 className="admin-section-title">Book Management</h3>
          <p className="admin-section-subtitle">
            {filteredBooks.length} of {pendingBooks.length} books shown
          </p>
        </div>
        {filteredBooks.length > 0 && (
          <button
            onClick={() => {
              if (window.confirm('Approve all visible books?')) {
                filteredBooks.forEach(book => handleApproveBook(book.id));
              }
            }}
            className="btn btn-success"
          >
            <CheckCircle size={16} />
            Approve All Visible
          </button>
        )}
      </div>

      <SearchAndFilter
        onSearch={handleSearch}
        onFilter={handleFilter}
        placeholder="Search books by title, author, or vendor..."
      />

      {filteredBooks.length === 0 ? (
        <div className="empty-state">
          <Book className="empty-icon" size={64} />
          <h3 className="empty-title">
            {searchTerm || filterStatus !== 'all' ? 'No books match your filters' : 'No pending books'}
          </h3>
          <p className="empty-description">
            {searchTerm || filterStatus !== 'all' ? 'Try adjusting your search or filters' : 'All books have been reviewed'}
          </p>
        </div>
      ) : (
        <div className="admin-content-list">
          {filteredBooks.map((book) => (
            <div key={book.id} className="admin-book-item">
              <div className="admin-book-content">
                <img
                  src={book.imageUrl || `https://via.placeholder.com/100x140/4f46e5/ffffff?text=${encodeURIComponent(book.title)}`}
                  alt={book.title}
                  className="admin-book-image"
                />
                <div className="admin-book-details">
                  <div className="admin-book-header">
                    <div>
                      <h4 className="admin-book-title">{book.title}</h4>
                      <p className="admin-book-author">by {book.author}</p>
                      <p className="admin-book-vendor">
                        Vendor: {book.vendor?.businessName || 'Unknown'}
                      </p>
                      {book.isbn && (
                        <p className="admin-book-isbn">ISBN: {book.isbn}</p>
                      )}
                    </div>
                    <div className="admin-book-price-info">
                      <p className="admin-book-price">${book.price}</p>
                      <p className="admin-book-stock">Stock: {book.stockQuantity}</p>
                      <span className={`admin-status-badge ${book.isApproved ? 'approved' : 'pending'}`}>
                        {book.isApproved ? 'Approved' : 'Pending'}
                      </span>
                    </div>
                  </div>

                  {book.description && (
                    <div className="admin-book-description">
                      <p>
                        {book.description.length > 200 ?
                          book.description.substring(0, 200) + '...' :
                          book.description
                        }
                      </p>
                    </div>
                  )}

                  <div className="admin-book-actions">
                    <button
                      onClick={() => handleApproveBook(book.id)}
                      disabled={loading || book.isApproved}
                      className="btn btn-success"
                    >
                      <CheckCircle size={16} />
                      {book.isApproved ? 'Approved' : 'Approve'}
                    </button>
                    <button
                      onClick={() => handleDisapproveBook(book.id)}
                      disabled={loading}
                      className="btn btn-danger"
                    >
                      <XCircle size={16} />
                      Reject
                    </button>
                    <button
                      onClick={() => {
                        alert('Book details: ' + JSON.stringify(book, null, 2));
                      }}
                      className="btn btn-outline"
                    >
                      <Eye size={16} />
                      View Details
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

  const renderPendingVendors = () => (
    <div className="admin-section">
      <div className="admin-section-header">
        <div>
          <h3 className="admin-section-title">Vendor Management</h3>
          <p className="admin-section-subtitle">
            {filteredVendors.length} of {pendingVendors.length} vendors shown
          </p>
        </div>
      </div>

      <SearchAndFilter
        onSearch={handleSearch}
        onFilter={handleFilter}
        placeholder="Search vendors by business name or email..."
      />

      {filteredVendors.length === 0 ? (
        <div className="empty-state">
          <Store className="empty-icon" size={64} />
          <h3 className="empty-title">
            {searchTerm || filterStatus !== 'all' ? 'No vendors match your filters' : 'No pending vendors'}
          </h3>
          <p className="empty-description">
            {searchTerm || filterStatus !== 'all' ? 'Try adjusting your search or filters' : 'All vendors are reviewed'}
          </p>
        </div>
      ) : (
        <div className="admin-content-list">
          {filteredVendors.map((vendor) => {
            // Debug logging to see vendor object structure
            console.log('Vendor object:', vendor);

            // Try different possible ID field names
            const vendorId = vendor.vendorId || vendor.id || vendor.venderId;

            return (
              <div key={vendorId || vendor.businessEmail} className="admin-vendor-item">
                <div className="admin-vendor-content">
                  <div className="admin-vendor-info">
                    <h4 className="admin-vendor-name">{vendor.businessName}</h4>
                    <p className="admin-vendor-email">Email: {vendor.businessEmail}</p>
                    <p className="admin-vendor-phone">Phone: {vendor.businessPhone}</p>
                    <p className="admin-vendor-owner">
                      Owner: {vendor.user?.firstName} {vendor.user?.lastName}
                    </p>
                    <p className="admin-vendor-date">
                      Registered: {new Date(vendor.createdAt).toLocaleDateString()}
                    </p>
                    <span className={`admin-status-badge ${vendor.isApproved ? 'approved' : 'pending'}`}>
                      {vendor.isApproved ? 'Approved' : 'Pending'}
                    </span>
                  </div>
                  <div className="admin-vendor-actions">
                    <button
                      onClick={() => {
                        console.log('Approving vendor with ID:', vendorId);
                        if (vendorId) {
                          handleApproveVendor(vendorId);
                        } else {
                          alert('Error: Vendor ID is missing');
                        }
                      }}
                      disabled={loading || !vendorId || vendor.isApproved}
                      className="btn btn-success"
                    >
                      <CheckCircle size={16} />
                      {vendor.isApproved ? 'Approved' : 'Approve'}
                    </button>
                    <button
                      onClick={() => {
                        console.log('Disapproving vendor with ID:', vendorId);
                        if (vendorId) {
                          handleDisapproveVendor(vendorId);
                        } else {
                          alert('Error: Vendor ID is missing');
                        }
                      }}
                      disabled={loading || !vendorId}
                      className="btn btn-danger"
                    >
                      <XCircle size={16} />
                      Disapprove
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );

  const renderUsers = () => (
    <div className="admin-section">
      <div className="admin-section-header">
        <div>
          <h3 className="admin-section-title">User Management</h3>
          <p className="admin-section-subtitle">
            {filteredUsers.length} of {allUsers.length} users shown
          </p>
        </div>
      </div>

      <SearchAndFilter
        onSearch={handleSearch}
        onFilter={handleFilter}
        placeholder="Search users by name or email..."
      />

      {filteredUsers.length === 0 ? (
        <div className="empty-state">
          <Users className="empty-icon" size={64} />
          <h3 className="empty-title">
            {searchTerm || filterStatus !== 'all' ? 'No users match your filters' : 'No users found'}
          </h3>
          <p className="empty-description">
            {searchTerm || filterStatus !== 'all' ? 'Try adjusting your search or filters' : 'Loading users...'}
          </p>
        </div>
      ) : (
        <div className="admin-content-list">
          {filteredUsers.slice(0, 20).map((userItem) => (
            <div key={userItem.id} className="admin-user-item">
              <div className="admin-user-content">
                <div className="admin-user-info">
                  <h4 className="admin-user-name">
                    {userItem.firstName} {userItem.lastName}
                  </h4>
                  <p className="admin-user-email">{userItem.email}</p>
                  <p className="admin-user-date">
                    Joined: {new Date(userItem.createdAt).toLocaleDateString()}
                  </p>
                  <span className={`admin-status-badge ${userItem.isVerified ? 'approved' : 'pending'}`}>
                    {userItem.isVerified ? 'Verified' : 'Unverified'}
                  </span>
                </div>
                <div className="admin-user-role">
                  <select
                    value={userItem.role}
                    onChange={(e) => handleUpdateUserRole(userItem.id, e.target.value)}
                    className="admin-role-select"
                  >
                    <option value="USER">User</option>
                    <option value="VENDOR">Vendor</option>
                    <option value="ADMIN">Admin</option>
                  </select>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );

  const renderOrders = () => (
    <div className="admin-section">
      <div className="admin-section-header">
        <div>
          <h3 className="admin-section-title">Order Management</h3>
          <p className="admin-section-subtitle">{allOrders.length} total orders</p>
        </div>
      </div>

      <div className="admin-coming-soon">
        <h4>Order Management Coming Soon</h4>
        <p>This feature will be available in the next update.</p>
      </div>
    </div>
  );

  return (
    <div className="admin-dashboard">
      <div className="container">
        <div className="admin-page-header">
          <h2 className="admin-page-title">Admin Dashboard</h2>
          <p className="admin-page-subtitle">Manage your bookstore operations</p>
        </div>

        <div className="admin-tabs">
          <div className="admin-tabs-container">
            <TabButton id="overview" label="Overview" icon={TrendingUp} />
            <TabButton
              id="books"
              label="Books"
              count={pendingBooks.length > 0 ? pendingBooks.length : null}
              icon={Book}
            />
            <TabButton
              id="vendors"
              label="Vendors"
              count={pendingVendors.length > 0 ? pendingVendors.length : null}
              icon={Store}
            />
            <TabButton id="users" label="Users" icon={Users} />
            <TabButton id="orders" label="Orders" icon={ShoppingCart} />
          </div>
        </div>

        {activeTab === 'overview' && renderOverview()}
        {activeTab === 'books' && renderPendingBooks()}
        {activeTab === 'vendors' && renderPendingVendors()}
        {activeTab === 'users' && renderUsers()}
        {activeTab === 'orders' && renderOrders()}
      </div>
    </div>
  );
};

export default AdminDashboard;