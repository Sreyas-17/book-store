// src/services/adminService.js
import { adminApi } from '../api/adminApi';

export const adminService = {
  // Dashboard
  async getDashboard() {
    return await adminApi.getDashboard();
  },

  // Book management
  async getAllBooks() {
    return await adminApi.getAllBooks();
  },

  async getPendingBooks() {
    return await adminApi.getPendingBooks();
  },

  async approveBook(bookId) {
    return await adminApi.approveBook(bookId);
  },

  async disapproveBook(bookId) {
    return await adminApi.disapproveBook(bookId);
  },

  // Vendor management
  async getAllVendors() {
    return await adminApi.getAllVendors();
  },

  async getPendingVendors() {
    return await adminApi.getPendingVendors();
  },

  async approveVendor(vendorId) {
    return await adminApi.approveVendor(vendorId);
  },

  async disapproveVendor(vendorId) {
    return await adminApi.disapproveVendor(vendorId);
  },

  // Review management
  async approveReview(reviewId) {
    return await adminApi.approveReview(reviewId);
  },

  // User management
  async getAllUsers() {
    return await adminApi.getAllUsers();
  },

  async updateUserRole(userId, role) {
    return await adminApi.updateUserRole(userId, role);
  },

  // Order management
  async getAllOrders() {
    return await adminApi.getAllOrders();
  }
};