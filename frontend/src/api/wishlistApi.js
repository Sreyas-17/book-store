import apiClient from './axiosConfig';

export const wishlistApi = {
  get: (userId) => apiClient.get(`/wishlist/${userId}`),
  add: (userId, bookId) =>
    apiClient.post(`/wishlist/add?userId=${userId}&bookId=${bookId}`),
  remove: (userId, bookId) =>
    apiClient.delete(`/wishlist/remove?userId=${userId}&bookId=${bookId}`),
  moveToCart: (userId, bookId) =>
    apiClient.post(`/wishlist/move-to-cart?userId=${userId}&bookId=${bookId}`)
};