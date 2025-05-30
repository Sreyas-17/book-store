import apiClient from './axiosConfig';

export const categoryApi = {
  getAll: () => apiClient.get('/categories'),
  getActive: () => apiClient.get('/categories/active'),
  create: (categoryData) => apiClient.post('/categories', categoryData),
  update: (categoryId, categoryData) => apiClient.put(`/categories/${categoryId}`, categoryData),
  delete: (categoryId) => apiClient.delete(`/categories/${categoryId}`)
};