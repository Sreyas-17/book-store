import React from 'react';

const AdminStats = ({ stats }) => {
  const statCards = [
    { label: 'Total Books', value: stats.totalBooks || 0, color: '#3498db' },
    { label: 'Approved Books', value: stats.approvedBooks || 0, color: '#27ae60' },
    { label: 'Pending Books', value: stats.pendingBooks || 0, color: '#f39c12' },
    { label: 'Total Reviews', value: stats.totalReviews || 0, color: '#9b59b6' },
    { label: 'Approved Reviews', value: stats.approvedReviews || 0, color: '#27ae60' },
    { label: 'Pending Reviews', value: stats.pendingReviews || 0, color: '#e74c3c' }
  ];

  return (
    <div className="admin-stats">
      <h2>Dashboard Overview</h2>
      <div className="stats-grid">
        {statCards.map((stat, index) => (
          <div key={index} className="stat-card" style={{ borderLeftColor: stat.color }}>
            <h3>{stat.label}</h3>
            <p className="stat-number">{stat.value}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AdminStats;
