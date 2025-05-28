import { cartApi } from '../api/cartApi';

export const cartService = {
  async getCart(userId) {
    return await cartApi.get(userId);
  },

  async addToCart(userId, bookId, quantity = 1) {
    return await cartApi.add(userId, bookId, quantity);
  },

  async removeFromCart(userId, bookId) {
    return await cartApi.remove(userId, bookId);
  },

  async updateQuantity(userId, bookId, quantity) {
    return await cartApi.updateQuantity(userId, bookId, quantity);
  }
};