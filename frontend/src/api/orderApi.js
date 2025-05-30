import apiClient from './axiosConfig';

export const orderApi = {
  // User operations
  getUserOrders: (userId) => apiClient.get(`/orders/user/${userId}`),
  create: (userId, addressId) =>
    apiClient.post(`/orders/create?userId=${userId}&addressId=${addressId}`),
  getById: (orderId) => apiClient.get(`/orders/${orderId}`),

  // Admin operations
  updateStatus: (orderId, status) =>
    apiClient.put(`/orders/${orderId}/status?status=${status}`)
};