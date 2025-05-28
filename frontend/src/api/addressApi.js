import apiClient from './axiosConfig';

export const addressApi = {
  getUserAddresses: (userId) => apiClient.get(`/addresses/user/${userId}`),
  add: (addressData) => apiClient.post('/addresses/add', addressData),
  update: (addressId, addressData) => apiClient.put(`/addresses/${addressId}`, addressData),
  delete: (addressId) => apiClient.delete(`/addresses/${addressId}`)
};