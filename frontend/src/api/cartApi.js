import apiClient from './axiosConfig';

export const cartApi = {
  get: (userId) => apiClient.get(`/cart/${userId}`),
  add: (userId, bookId, quantity) => 
    apiClient.post(`/cart/add?userId=${userId}&bookId=${bookId}&quantity=${quantity}`),
  remove: (userId, bookId) => 
    apiClient.delete(`/cart/remove?userId=${userId}&bookId=${bookId}`),
  updateQuantity: (userId, bookId, quantity) => 
    apiClient.put(`/cart/update-quantity?userId=${userId}&bookId=${bookId}&quantity=${quantity}`)
};