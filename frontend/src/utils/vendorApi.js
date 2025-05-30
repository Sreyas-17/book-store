import axiosConfig from '../api/axiosConfig';

export const vendorApi = {
  // Get vendor's books
  getVendorBooks: async () => {
    const response = await axiosConfig.get('/books/vendor');
    return response.data;
  },

  // Add new book
  addBook: async (bookData) => {
    const response = await axiosConfig.post('/books', bookData);
    return response.data;
  },

  // Update book
  updateBook: async (bookId, bookData) => {
    const response = await axiosConfig.put(`/books/${bookId}`, bookData);
    return response.data;
  },

  // Delete book
  deleteBook: async (bookId) => {
    const response = await axiosConfig.delete(`/books/${bookId}`);
    return response.data;
  }
};
