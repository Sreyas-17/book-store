import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useVendor } from '../hooks/useVendor';
import VendorBooks from '../vendor/VendorBooks';
import VendorStats from '../vendor/VendorBooks';
import AddBookModal from '../vendor/VendorBooks';
import '../index.css';

const VendorDashboard = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('books');
  const [showAddModal, setShowAddModal] = useState(false);
  const [editingBook, setEditingBook] = useState(null);
  
  const {
    books,
    stats,
    loading,
    error,
    addBook,
    updateBook,
    deleteBook
  } = useVendor();

  if (user?.role !== 'VENDOR') {
    return <div className="access-denied">Access Denied: Vendor privileges required</div>;
  }

  const handleAddBook = () => {
    setEditingBook(null);
    setShowAddModal(true);
  };

  const handleEditBook = (book) => {
    setEditingBook(book);
    setShowAddModal(true);
  };

  const handleSaveBook = async (bookData) => {
    const result = editingBook 
      ? await updateBook(editingBook.id, bookData)
      : await addBook(bookData);
    
    if (result.success) {
      setShowAddModal(false);
      setEditingBook(null);
      alert(result.message);
    } else {
      alert(result.message);
    }
  };

  const handleDeleteBook = async (bookId) => {
    if (window.confirm('Are you sure you want to delete this book?')) {
      const result = await deleteBook(bookId);
      alert(result.message);
    }
  };

  const tabs = [
    { id: 'books', label: 'My Books', count: books.length },
    { id: 'analytics', label: 'Analytics', count: null }
  ];

  return (
    <div className="vendor-dashboard">
      <h1>Vendor Dashboard</h1>
      
      <div className="vendor-tabs">
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

      {activeTab === 'books' && (
        <VendorBooks 
          books={books}
          loading={loading}
          onAddBook={handleAddBook}
          onEditBook={handleEditBook}
          onDeleteBook={handleDeleteBook}
        />
      )}
      
      {activeTab === 'analytics' && (
        <VendorStats stats={stats} books={books} />
      )}

      {showAddModal && (
        <AddBookModal 
          book={editingBook}
          onSave={handleSaveBook}
          onCancel={() => {
            setShowAddModal(false);
            setEditingBook(null);
          }}
        />
      )}
    </div>
  );
};

export default VendorDashboard;
