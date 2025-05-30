import apiClient from './axiosConfig';

export const bookApi = {
  // Public endpoints
  getAll: () => apiClient.get('/books/all'),
  searchByTitle: (title) => apiClient.get(`/books/search/title?title=${encodeURIComponent(title)}`),
  searchByAuthor: (author) => apiClient.get(`/books/search/author?author=${encodeURIComponent(author)}`),
  searchByRating: (rating) => apiClient.get(`/books/search/rating?rating=${rating}`),
  getPaginated: (page = 0, size = 10) => apiClient.get(`/books/paginated?page=${page}&size=${size}`),
  getSortedByPrice: (order = 'asc', page = 0, size = 10) =>
    apiClient.get(`/books/sort/price-${order}?page=${page}&size=${size}`),
  getNewArrivals: (page = 0, size = 10) => apiClient.get(`/books/new-arrivals?page=${page}&size=${size}`),

  // User operations
  rate: (bookId, ratingData) => apiClient.post(`/books/${bookId}/rate`, ratingData),

  // Admin operations
  updateImage: (bookId, imageUrl) => apiClient.put(`/books/${bookId}/image?imageUrl=${encodeURIComponent(imageUrl)}`)
};