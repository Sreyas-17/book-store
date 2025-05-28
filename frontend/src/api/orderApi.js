import apiClient from './axiosConfig';

export const orderApi = {
  getUserOrders: (userId) => apiClient.get(`/orders/user/${userId}`),
  create: (userId, addressId) => 
    apiClient.post(`/orders/create?userId=${userId}&addressId=${addressId}`)
};