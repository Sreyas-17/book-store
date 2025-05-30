import axiosConfig from '../api/axiosConfig';

export const adminApi = {
  // Get all books
  getAllBooks: async () => {
    const response = await axiosConfig.get('/admin/books');
    return response.data;
  },

  // Get pending books
  getPendingBooks: async () => {
    const response = await axiosConfig.get('/admin/books/pending');
    return response.data;
  },

  // Approve/reject book
  approveBook: async (bookId, approved) => {
    const response = await axiosConfig.put(`/admin/books/${bookId}/approve`, { approved });
    return response.data;
  },

  // Get all reviews
  getAllReviews: async () => {
    const response = await axiosConfig.get('/admin/reviews');
    return response.data;
  },

  // Get pending reviews
  getPendingReviews: async () => {
    const response = await axiosConfig.get('/admin/reviews/pending');
    return response.data;
  },

  // Approve/reject review
  approveReview: async (reviewId, approved) => {
    const response = await axiosConfig.put(`/admin/reviews/${reviewId}/approve`, { approved });
    return response.data;
  }
};
