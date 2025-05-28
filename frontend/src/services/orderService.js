import { orderApi } from '../api/orderApi';

export const orderService = {
  async getUserOrders(userId) {
    return await orderApi.getUserOrders(userId);
  },

  async createOrder(userId, addressId) {
    return await orderApi.create(userId, addressId);
  }
};