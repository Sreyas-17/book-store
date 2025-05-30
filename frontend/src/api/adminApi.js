import apiClient from './axiosConfig';

export const adminApi = {
  // Dashboard
  getDashboard: () => apiClient.get('/admin/dashboard'),

  // Book management
  getAllBooks: () => apiClient.get('/admin/books/all'),
  getPendingBooks: () => apiClient.get('/admin/books/pending'),
  approveBook: (bookId) => apiClient.put(`/admin/books/${bookId}/approve`),
  disapproveBook: (bookId) => apiClient.put(`/admin/books/${bookId}/disapprove`),

  // Vendor management
  getAllVendors: () => apiClient.get('/admin/vendors/all'),
  getPendingVendors: () => apiClient.get('/admin/vendors/pending'),
  approveVendor: (vendorId) => apiClient.put(`/admin/vendors/${vendorId}/approve`),
  disapproveVendor: (vendorId) => apiClient.put(`/admin/vendors/${vendorId}/disapprove`),

  // Review management
  approveReview: (reviewId) => apiClient.put(`/admin/reviews/${reviewId}/approve`),

  // User management
  getAllUsers: () => apiClient.get('/admin/users'),
  updateUserRole: (userId, role) => apiClient.put(`/admin/users/${userId}/role?role=${role}`),

  // Order management
  getAllOrders: () => apiClient.get('/admin/orders')
};