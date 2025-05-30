import apiClient from './axiosConfig';

export const vendorApi = {
  // Vendor profile management
  register: (vendorData) => apiClient.post('/vendor/register', vendorData),
  getProfile: () => apiClient.get('/vendor/profile'),
  updateProfile: (vendorData) => apiClient.put('/vendor/profile', vendorData),

  // Book management
  addBook: (bookData) => apiClient.post('/vendor/books', bookData),
  getBooks: () => apiClient.get('/vendor/books'),
  updateBook: (bookId, bookData) => apiClient.put(`/vendor/books/${bookId}`, bookData),
  deleteBook: (bookId) => apiClient.delete(`/vendor/books/${bookId}`),
  updateBookImage: (bookId, imageUrl) =>
    apiClient.put(`/vendor/books/${bookId}/image?imageUrl=${encodeURIComponent(imageUrl)}`),

  // Order management
  getOrders: () => apiClient.get('/vendor/orders'),
  updateOrderStatus: (orderId, status) =>
    apiClient.put(`/vendor/orders/${orderId}/status?status=${status}`),

  // Dashboard
  getDashboard: () => apiClient.get('/vendor/dashboard')
};