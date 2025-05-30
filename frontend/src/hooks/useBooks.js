import { useState, useEffect, useCallback } from 'react';
import { bookService } from '../services/bookService';
import { useAuth } from '../contexts/AuthContext';

export const useBooks = () => {
  const [books, setBooks] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const { user } = useAuth();

  const fetchBooks = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await bookService.getAllBooks();
      if (response.success) {
        setBooks(Array.isArray(response.data) ? response.data : []);
      } else {
        throw new Error(response.message || 'Failed to fetch books');
      }
    } catch (error) {
      console.error('Error fetching books:', error);
      setError(error.message);
      setBooks([]);
    } finally {
      setLoading(false);
    }
  }, []);

  const searchBooks = useCallback(async (term) => {
    if (!term?.trim()) {
      fetchBooks();
      return;
    }

    setLoading(true);
    setError(null);
    setSearchTerm(term);

    try {
      const response = await bookService.searchBooks(term);
      if (response.success) {
        setBooks(Array.isArray(response.data) ? response.data : []);
      } else {
        throw new Error(response.message || 'Search failed');
      }
    } catch (error) {
      console.error('Error searching books:', error);
      setError(error.message);
      setBooks([]);
    } finally {
      setLoading(false);
    }
  }, [fetchBooks]);

  const searchByAuthor = useCallback(async (term) => {
    if (!term?.trim()) {
      fetchBooks();
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const response = await bookService.searchBooksByAuthor(term);
      if (response.success) {
        setBooks(Array.isArray(response.data) ? response.data : []);
      } else {
        throw new Error(response.message || 'Author search failed');
      }
    } catch (error) {
      console.error('Error searching books by author:', error);
      setError(error.message);
      setBooks([]);
    } finally {
      setLoading(false);
    }
  }, [fetchBooks]);

  const searchByTitle = useCallback(async (term) => {
    if (!term?.trim()) {
      fetchBooks();
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const response = await bookService.searchBooksByTitle(term);
      if (response.success) {
        setBooks(Array.isArray(response.data) ? response.data : []);
      } else {
        throw new Error(response.message || 'Title search failed');
      }
    } catch (error) {
      console.error('Error searching books by title:', error);
      setError(error.message);
      setBooks([]);
    } finally {
      setLoading(false);
    }
  }, [fetchBooks]);

  const rateBook = useCallback(async (bookId, rating) => {
    if (!user?.id) {
      return { success: false, message: 'Please login first' };
    }

    try {
      const response = await bookService.rateBook(bookId, user.id, rating);
      if (response.success) {
        await fetchBooks(); // Refresh books to get updated ratings
      }
      return response;
    } catch (error) {
      console.error('Error rating book:', error);
      return { success: false, message: 'Failed to rate book' };
    }
  }, [user?.id, fetchBooks]);

  useEffect(() => {
    fetchBooks();
  }, [fetchBooks]);

  return {
    books,
    searchTerm,
    loading,
    error,
    setSearchTerm,
    fetchBooks,
    searchBooks,
    searchByAuthor,
    searchByTitle,
    rateBook
  };
};
