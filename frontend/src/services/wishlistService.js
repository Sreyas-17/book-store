import { wishlistApi } from '../api/wishlistApi';

export const wishlistService = {
  async getWishlist(userId) {
    return await wishlistApi.get(userId);
  },

  async addToWishlist(userId, bookId) {
    return await wishlistApi.add(userId, bookId);
  },

  async removeFromWishlist(userId, bookId) {
    return await wishlistApi.remove(userId, bookId);
  }
};
