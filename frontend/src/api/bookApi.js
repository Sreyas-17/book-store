import apiClient from './axiosConfig';

export const bookApi = {
  getAll: () => apiClient.get('/books/all'),
  searchByTitle: (title) => apiClient.get(`/books/search/title?title=${encodeURIComponent(title)}`),
  searchByAuthor: (author) => apiClient.get(`/books/search/author?author=${encodeURIComponent(author)}`),
  rate: (bookId, ratingData) => apiClient.post(`/books/${bookId}/rate`, ratingData)
};