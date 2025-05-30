import { vendorApi } from '../utils/vendorApi';

class VendorService {
  async getVendorBooks() {
    try {
      return await vendorApi.getVendorBooks();
    } catch (error) {
      console.error('Error fetching vendor books:', error);
      throw error;
    }
  }

  async addBook(bookData) {
    try {
      const processedData = this.processBookData(bookData);
      return await vendorApi.addBook(processedData);
    } catch (error) {
      console.error('Error adding book:', error);
      throw error;
    }
  }

  async updateBook(bookId, bookData) {
    try {
      const processedData = this.processBookData(bookData);
      return await vendorApi.updateBook(bookId, processedData);
    } catch (error) {
      console.error('Error updating book:', error);
      throw error;
    }
  }

  async deleteBook(bookId) {
    try {
      return await vendorApi.deleteBook(bookId);
    } catch (error) {
      console.error('Error deleting book:', error);
      throw error;
    }
  }

  processBookData(bookData) {
    return {
      ...bookData,
      price: parseFloat(bookData.price),
      stockQuantity: parseInt(bookData.stockQuantity)
    };
  }

  calculateVendorStats(books) {
    return {
      totalBooks: books.length,
      approvedBooks: books.filter(book => book.approved).length,
      pendingBooks: books.filter(book => !book.approved).length,
      totalStock: books.reduce((sum, book) => sum + book.stockQuantity, 0),
      averageRating: books.length > 0 
        ? (books.reduce((sum, book) => sum + (book.averageRating || 0), 0) / books.length).toFixed(1)
        : 'N/A'
    };
  }
}

export default new VendorService();
