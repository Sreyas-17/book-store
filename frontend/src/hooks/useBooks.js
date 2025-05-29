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

  // General search that searches both title and author
  const searchBooks = async (term) => {
    if (!term.trim()) {
      fetchBooks();
      return;
    }
    
    try {
      // Search both title and author, then combine results
      const [titleResponse, authorResponse] = await Promise.all([
        bookService.searchBooksByTitle(term),
        bookService.searchBooksByAuthor(term)
      ]);
      
      let allBooks = [];
      
      if (titleResponse.success) {
        allBooks = [...allBooks, ...titleResponse.data];
      }
      
      if (authorResponse.success) {
        allBooks = [...allBooks, ...authorResponse.data];
      }
      
      // Remove duplicates based on book ID
      const uniqueBooks = allBooks.filter((book, index, self) => 
        index === self.findIndex(b => b.id === book.id)
      );
      
      setBooks(uniqueBooks);
    } catch (error) {
      console.error('Error searching books:', error);
      // Fallback to your original method if the new endpoints don't exist yet
      try {
        const response = await bookService.searchBooks(term);
        if (response.success) {
          setBooks(response.data);
        }
      } catch (fallbackError) {
        console.error('Fallback search also failed:', fallbackError);
      }
    }
  };

  // Specific author search
  const searchByAuthor = async (term) => {
    if (!term.trim()) {
      fetchBooks();
      return;
    }
    
    try {
      const response = await bookService.searchBooksByAuthor(term);
      if (response.success) {
        setBooks(response.data);
      }
    } catch (error) {
      console.error('Error searching books by author:', error);
    }
  };

  // Optional: Add a specific title search function
  const searchByTitle = async (term) => {
    if (!term.trim()) {
      fetchBooks();
      return;
    }
    
    try {
      const response = await bookService.searchBooksByTitle(term);
      if (response.success) {
        setBooks(response.data);
      }
    } catch (error) {
      console.error('Error searching books by title:', error);
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
      return { success: false, message: 'Failed torate book' };
    }
  };

  return {
    books,
    searchTerm,
    setSearchTerm,
    fetchBooks,
    searchBooks,      // Searches both title and author
    searchByAuthor,   // Searches only by author
    searchByTitle,    // Searches only by title (optional)
    rateBook
  };
};