import { addressApi } from '../api/addressApi';

export const addressService = {
  async getUserAddresses(userId) {
    return await addressApi.getUserAddresses(userId);
  },

  async addAddress(addressData) {
    return await addressApi.add(addressData);
  },

  async updateAddress(addressId, addressData) {
    return await addressApi.update(addressId, addressData);
  },

  async deleteAddress(addressId) {
    return await addressApi.delete(addressId);
  }
};