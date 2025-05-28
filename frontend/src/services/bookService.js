import { bookApi } from '../api/bookApi';

export const bookService = {
  async getAllBooks() {
    return await bookApi.getAll();
  },

  async searchBooks(title) {
    return await bookApi.searchByTitle(title);
  },

  async rateBook(bookId, userId, rating) {
    return await bookApi.rate(bookId, { userId, rating });
  }
};