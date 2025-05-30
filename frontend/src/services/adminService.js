import { adminApi } from '../utils/adminapi';

class AdminService {
  async getAllBooks() {
    try {
      return await adminApi.getAllBooks();
    } catch (error) {
      console.error('Error fetching all books:', error);
      throw error;
    }
  }

  async getPendingBooks() {
    try {
      return await adminApi.getPendingBooks();
    } catch (error) {
      console.error('Error fetching pending books:', error);
      throw error;
    }
  }

  async approveBook(bookId, approved) {
    try {
      return await adminApi.approveBook(bookId, approved);
    } catch (error) {
      console.error('Error approving book:', error);
      throw error;
    }
  }

  async getAllReviews() {
    try {
      return await adminApi.getAllReviews();
    } catch (error) {
      console.error('Error fetching all reviews:', error);
      throw error;
    }
  }

  async getPendingReviews() {
    try {
      return await adminApi.getPendingReviews();
    } catch (error) {
      console.error('Error fetching pending reviews:', error);
      throw error;
    }
  }

  async approveReview(reviewId, approved) {
    try {
      return await adminApi.approveReview(reviewId, approved);
    } catch (error) {
      console.error('Error approving review:', error);
      throw error;
    }
  }

  async getAdminStats() {
    try {
      const [books, reviews, pendingBooks, pendingReviews] = await Promise.all([
        this.getAllBooks(),
        this.getAllReviews(),
        this.getPendingBooks(),
        this.getPendingReviews()
      ]);

      return {
        totalBooks: books.length,
        totalReviews: reviews.length,
        pendingBooks: pendingBooks.length,
        pendingReviews: pendingReviews.length,
        approvedBooks: books.filter(book => book.approved).length,
        approvedReviews: reviews.filter(review => review.approved).length
      };
    } catch (error) {
      console.error('Error fetching admin stats:', error);
      throw error;
    }
  }
}

export default new AdminService();
