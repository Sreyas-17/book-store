import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useAdmin } from '../hooks/useAdmin';
import BookManagement from '../admin/BookManagement';
import ReviewManagement from '../admin/BookManagement';
import PendingApprovals from '../admin/BookManagement';
import AdminStats from '../admin/BookManagement';


const AdminDashboard = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('stats');
  const {
    books,
    reviews,
    pendingBooks,
    pendingReviews,
    stats,
    loading,
    error,
    approveBook,
    approveReview
  } = useAdmin();

  if (user?.role !== 'ADMIN') {
    return <div className="access-denied">Access Denied: Admin privileges required</div>;
  }

  const tabs = [
    { id: 'stats', label: 'Dashboard', count: null },
    { id: 'books', label: 'Books', count: books.length },
    { id: 'reviews', label: 'Reviews', count: reviews.length },
    { id: 'pending', label: 'Pending', count: pendingBooks.length + pendingReviews.length }
  ];

  return (
    <div className="admin-dashboard">
      <h1>Admin Dashboard</h1>
      
      <div className="admin-tabs">
        {tabs.map(tab => (
          <button 
            key={tab.id}
            className={activeTab === tab.id ? 'active' : ''}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.label} {tab.count !== null && `(${tab.count})`}
          </button>
        ))}
      </div>

      {error && <div className="error-message">{error}</div>}
      {loading && <div className="loading">Loading...</div>}

      {activeTab === 'stats' && <AdminStats stats={stats} />}
      
      {activeTab === 'books' && (
        <BookManagement 
          books={books} 
          onApprove={approveBook}
          loading={loading}
        />
      )}
      
      {activeTab === 'reviews' && (
        <ReviewManagement 
          reviews={reviews} 
          onApprove={approveReview}
          loading={loading}
        />
      )}
      
      {activeTab === 'pending' && (
        <PendingApprovals 
          pendingBooks={pendingBooks}
          pendingReviews={pendingReviews}
          onApproveBook={approveBook}
          onApproveReview={approveReview}
          loading={loading}
        />
      )}
    </div>
  );
};

export default AdminDashboard;
