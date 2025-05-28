import { useState, useEffect } from 'react';
import { bookService } from '../services/bookService';
import { useAuth } from '../contexts/AuthContext';

export const useBooks = () => {
  const [books, setBooks] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const { user, token } = useAuth();

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    try {
      const response = await bookService.getAllBooks();
      if (response.success) {
        setBooks(response.data);
        console.log('Books fetched:', response.data.length);
      }
    } catch (error) {
      console.error('Error fetching books:', error);
    }
  };

  const searchBooks = async (term) => {
    if (!term.trim()) {
      fetchBooks();
      return;
    }
    
    try {
      const response = await bookService.searchBooks(term);
      if (response.success) {
        setBooks(response.data);
      }
    } catch (error) {
      console.error('Error searching books:', error);
    }
  };

  const searchByAuthor = async (term) => {
    if (!term.trim()) {
      fetchBooks();
      return;
    }
    
    try {
      const response = await bookService.searchBooks(term);
      if (response.success) {
        // Filter books by author
        const authorBooks = response.data.filter(book => 
          book.author.toLowerCase().includes(term.toLowerCase())
        );
        setBooks(authorBooks);
      }
    } catch (error) {
      console.error('Error searching books by author:', error);
    }
  };

  const rateBook = async (bookId, rating) => {
    if (!user?.id) {
      return { success: false, message: 'Please login first' };
    }
    
    try {
      const response = await bookService.rateBook(bookId, user.id, rating);
      
      if (response.success) {
        await fetchBooks(); // Refresh books to get updated ratings
        return response;
      }
      
      return response;
    } catch (error) {
      console.error('Error rating book:', error);
      return { success: false, message: 'Failed to rate book' };
    }
  };

  return {
    books,
    searchTerm,
    setSearchTerm,
    fetchBooks,
    searchBooks,
    searchByAuthor,
    rateBook
  };
};