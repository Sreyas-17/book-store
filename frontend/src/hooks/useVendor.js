import { useState, useEffect } from 'react';
import vendorService from '../services/vendorService';

export const useVendor = () => {
  const [books, setBooks] = useState([]);
  const [stats, setStats] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchVendorBooks = async () => {
    setLoading(true);
    setError(null);
    try {
      const booksData = await vendorService.getVendorBooks();
      setBooks(booksData);
      setStats(vendorService.calculateVendorStats(booksData));
    } catch (err) {
      setError(err.message);
      console.error('Error fetching vendor books:', err);
    } finally {
      setLoading(false);
    }
  };

  const addBook = async (bookData) => {
    try {
      await vendorService.addBook(bookData);
      await fetchVendorBooks(); // Refresh data
      return { success: true, message: 'Book added successfully' };
    } catch (err) {
      setError(err.message);
      return { success: false, message: 'Error adding book' };
    }
  };

  const updateBook = async (bookId, bookData) => {
    try {
      await vendorService.updateBook(bookId, bookData);
      await fetchVendorBooks(); // Refresh data
      return { success: true, message: 'Book updated successfully' };
    } catch (err) {
      setError(err.message);
      return { success: false, message: 'Error updating book' };
    }
  };

  const deleteBook = async (bookId) => {
    try {
      await vendorService.deleteBook(bookId);
      await fetchVendorBooks(); // Refresh data
      return { success: true, message: 'Book deleted successfully' };
    } catch (err) {
      setError(err.message);
      return { success: false, message: 'Error deleting book' };
    }
  };

  useEffect(() => {
    fetchVendorBooks();
  }, []);

  return {
    books,
    stats,
    loading,
    error,
    addBook,
    updateBook,
    deleteBook,
    refreshBooks: fetchVendorBooks
  };
};
