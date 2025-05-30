import React from 'react';

const VendorStats = ({ stats, books }) => {
  const statCards = [
    { label: 'Total Books', value: stats.totalBooks || 0, color: '#3498db' },
    { label: 'Approved Books', value: stats.approvedBooks || 0, color: '#27ae60' },
    { label: 'Pending Books', value: stats.pendingBooks || 0, color: '#f39c12' },
    { label: 'Total Stock', value: stats.totalStock || 0, color: '#9b59b6' },
    { label: 'Average Rating', value: stats.averageRating || 'N/A', color: '#e67e22' }
  ];

  return (
    <div className="vendor-stats">
      <h2>Analytics</h2>
      <div className="stats-grid">
        {statCards.map((stat, index) => (
          <div key={index} className="stat-card" style={{ borderLeftColor: stat.color }}>
            <h3>{stat.label}</h3>
            <p className="stat-number">{stat.value}</p>
          </div>
        ))}
      </div>
      
      {books.length > 0 && (
        <div className="books-performance">
          <h3>Book Performance</h3>
          <div className="performance-list">
            {books
              .filter(book => book.averageRating > 0)
              .sort((a, b) => (b.averageRating || 0) - (a.averageRating || 0))
              .slice(0, 5)
              .map(book => (
                <div key={book.id} className="performance-item">
                  <span className="book-title">{book.title}</span>
                  <span className="book-rating">
                    {book.averageRating.toFixed(1)} ★
                  </span>
                </div>
              ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default VendorStats;
