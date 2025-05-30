import { useState, useEffect } from 'react';
import adminService from '../services/adminService';

export const useAdmin = () => {
  const [books, setBooks] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [pendingBooks, setPendingBooks] = useState([]);
  const [pendingReviews, setPendingReviews] = useState([]);
  const [stats, setStats] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchAllData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [booksData, reviewsData, pendingBooksData, pendingReviewsData, statsData] = await Promise.all([
        adminService.getAllBooks(),
        adminService.getAllReviews(),
        adminService.getPendingBooks(),
        adminService.getPendingReviews(),
        adminService.getAdminStats()
      ]);

      setBooks(booksData);
      setReviews(reviewsData);
      setPendingBooks(pendingBooksData);
      setPendingReviews(pendingReviewsData);
      setStats(statsData);
    } catch (err) {
      setError(err.message);
      console.error('Error fetching admin data:', err);
    } finally {
      setLoading(false);
    }
  };

  const approveBook = async (bookId, approved) => {
    try {
      await adminService.approveBook(bookId, approved);
      await fetchAllData(); // Refresh data
      return { success: true, message: `Book ${approved ? 'approved' : 'rejected'} successfully` };
    } catch (err) {
      setError(err.message);
      return { success: false, message: 'Error updating book approval' };
    }
  };

  const approveReview = async (reviewId, approved) => {
    try {
      await adminService.approveReview(reviewId, approved);
      await fetchAllData(); // Refresh data
      return { success: true, message: `Review ${approved ? 'approved' : 'rejected'} successfully` };
    } catch (err) {
      setError(err.message);
      return { success: false, message: 'Error updating review approval' };
    }
  };

  useEffect(() => {
    fetchAllData();
  }, []);

  return {
    books,
    reviews,
    pendingBooks,
    pendingReviews,
    stats,
    loading,
    error,
    approveBook,
    approveReview,
    refreshData: fetchAllData
  };
};
