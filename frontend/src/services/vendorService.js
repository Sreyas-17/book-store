// src/services/vendorService.js
import { vendorApi } from '../api/vendorApi';

export const vendorService = {
  // Vendor profile management
  async register(vendorData) {
    return await vendorApi.register(vendorData);
  },

  async getProfile() {
    return await vendorApi.getProfile();
  },

  async updateProfile(vendorData) {
    return await vendorApi.updateProfile(vendorData);
  },

  // Book management
  async addBook(bookData) {
    return await vendorApi.addBook(bookData);
  },

  async getBooks() {
    return await vendorApi.getBooks();
  },

  async updateBook(bookId, bookData) {
    return await vendorApi.updateBook(bookId, bookData);
  },

  async deleteBook(bookId) {
    return await vendorApi.deleteBook(bookId);
  },

  async updateBookImage(bookId, imageUrl) {
    return await vendorApi.updateBookImage(bookId, imageUrl);
  },

  // Order management
  async getOrders() {
    return await vendorApi.getOrders();
  },

  async updateOrderStatus(orderId, status) {
    return await vendorApi.updateOrderStatus(orderId, status);
  },

  // Dashboard
  async getDashboard() {
    return await vendorApi.getDashboard();
  }
};