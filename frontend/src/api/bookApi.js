import apiClient from './axiosConfig';

export const bookApi = {
  getAll: () => apiClient.get('/books/all'),
  searchByTitle: (title) => apiClient.get(`/books/search/title?title=${title}`),
  rate: (bookId, ratingData) => apiClient.post(`/books/${bookId}/rate`, ratingData)
};