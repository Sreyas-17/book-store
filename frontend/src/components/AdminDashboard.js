import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useAdmin } from '../hooks/useAdmin';
import BookManagement from '../admin/BookManagement';
import ReviewManagement from '../admin/ReviewManagement';
import PendingApprovals from '../admin/PendingApprovals';
import AdminStats from '../admin/AdminStats';

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
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="text-6xl mb-4">🚫</div>
          <h2 className="text-2xl font-bold text-gray-900 mb-2">Access Denied</h2>
          <p className="text-gray-600">Admin privileges required</p>
        </div>
      </div>
    );
  }

  const tabs = [
    { id: 'stats', label: 'Dashboard', count: null },
    { id: 'books', label: 'Books', count: books?.length || 0 },
    { id: 'reviews', label: 'Reviews', count: reviews?.length || 0 },
    { id: 'pending', label: 'Pending', count: (pendingBooks?.length || 0) + (pendingReviews?.length || 0) }
  ];

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="container-custom py-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Admin Dashboard</h1>
          <p className="text-gray-600">Manage books, reviews, and pending approvals</p>
        </div>
        
        {/* Tabs */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 mb-8">
          <div className="flex border-b border-gray-200">
            {tabs.map(tab => (
              <button 
                key={tab.id}
                className={`px-6 py-4 text-sm font-medium border-b-2 transition-colors duration-200 ${
                  activeTab === tab.id 
                    ? 'border-blue-500 text-blue-600 bg-blue-50' 
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:bg-gray-50'
                }`}
                onClick={() => setActiveTab(tab.id)}
              >
                {tab.label} {tab.count !== null && (
                  <span className="ml-2 px-2 py-1 text-xs bg-gray-100 text-gray-600 rounded-full">
                    {tab.count}
                  </span>
                )}
              </button>
            ))}
          </div>
        </div>

        {/* Error State */}
        {error && (
          <div className="alert alert-error mb-6">
            <span>⚠️</span>
            <span>{error}</span>
          </div>
        )}

        {/* Loading State */}
        {loading && (
          <div className="flex items-center justify-center py-12">
            <div className="loading-spinner w-8 h-8 mr-3"></div>
            <span className="text-gray-600">Loading...</span>
          </div>
        )}

        {/* Tab Content */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200">
          {activeTab === 'stats' && <AdminStats stats={stats} />}
          
          {activeTab === 'books' && (
            <BookManagement 
              books={books || []} 
              onApprove={approveBook}
              loading={loading}
            />
          )}
          
          {activeTab === 'reviews' && (
            <ReviewManagement 
              reviews={reviews || []} 
              onApprove={approveReview}
              loading={loading}
            />
          )}
          
          {activeTab === 'pending' && (
            <PendingApprovals 
              pendingBooks={pendingBooks || []}
              pendingReviews={pendingReviews || []}
              onApproveBook={approveBook}
              onApproveReview={approveReview}
              loading={loading}
            />
          )}
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
