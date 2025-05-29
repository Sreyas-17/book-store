import { bookApi } from '../api/bookApi';

export const bookService = {
  async getAllBooks() {
    return await bookApi.getAll();
  },

  // General search that searches both title and author
  async searchBooks(term) {
    try {
      // Search both title and author simultaneously
      const [titleResponse, authorResponse] = await Promise.all([
        bookApi.searchByTitle(term),
        bookApi.searchByAuthor(term)
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
      
      return {
        success: true,
        data: uniqueBooks,
        message: 'Books found'
      };
    } catch (error) {
      console.error('Error in searchBooks:', error);
      return {
        success: false,
        data: [],
        message: error.message || 'Search failed'
      };
    }
  },

  // Search specifically by title
  async searchBooksByTitle(title) {
    return await bookApi.searchByTitle(title);
  },

  // Search specifically by author
  async searchBooksByAuthor(author) {
    return await bookApi.searchByAuthor(author);
  },

  async rateBook(bookId, userId, rating) {
    return await bookApi.rate(bookId, { userId, rating });
  }
};