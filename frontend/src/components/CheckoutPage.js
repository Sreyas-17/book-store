import React, { useState } from 'react';
import { Plus, CreditCard } from 'lucide-react';
import { apiCall } from '../utils/api';

const CheckoutPage = ({ 
  user, 
  cart, 
  addresses, 
  token, 
  createOrder, 
  getCartTotal, 
  fetchAddresses 
}) => {
  const [selectedAddress, setSelectedAddress] = useState('');
  const [newAddress, setNewAddress] = useState({
    addressLine1: '',
    addressLine2: '',
    city: '',
    state: '',
    country: '',
    postalCode: '',
    default: false
  });
  const [showAddressForm, setShowAddressForm] = useState(false);

  const handleAddAddress = async (e) => {
    e.preventDefault();
    const response = await apiCall('/addresses/add', {
      method: 'POST',
      body: JSON.stringify({ ...newAddress, user: { id: user.id } }),
    }, token);
    
    if (response.success) {
      fetchAddresses();
      setShowAddressForm(false);
      setNewAddress({
        addressLine1: '',
        addressLine2: '',
        city: '',
        state: '',
        country: '',
        postalCode: '',
        default: false
      });
    }
  };

  const handlePlaceOrder = async () => {
    if (!selectedAddress) {
      alert('Please select a delivery address');
      return;
    }
    
    const response = await createOrder(selectedAddress);
    if (response.success) {
      alert('Order placed successfully!');
    }
  };

  return (
    <div className="container">
      <div className="page-header">
        <h2 className="page-title">Checkout</h2>
      </div>
      
      <div className="grid grid-cols-2" style={{gap: '32px'}}>
        <div>
          <h3 style={{fontSize: '20px', fontWeight: 'bold', color: '#1f2937', marginBottom: '16px'}}>Delivery Address</h3>
          
          <div className="space-y-4">
            {addresses.map((address) => (
              <div
                key={address.id}
                style={{
                  border: selectedAddress === address.id.toString() ? '2px solid #2563eb' : '2px solid #e5e7eb',
                  backgroundColor: selectedAddress === address.id.toString() ? '#eff6ff' : 'white',
                  borderRadius: '8px',
                  padding: '16px',
                  cursor: 'pointer',
                  transition: 'all 0.2s ease'
                }}
                onClick={() => setSelectedAddress(address.id.toString())}
              >
                <div style={{display: 'flex', alignItems: 'center', gap: '12px'}}>
                  <input
                    type="radio"
                    name="address"
                    value={address.id}
                    checked={selectedAddress === address.id.toString()}
                    onChange={() => setSelectedAddress(address.id.toString())}
                    style={{accentColor: '#2563eb'}}
                  />
                  <div>
                    <p style={{fontWeight: '600', marginBottom: '4px'}}>{address.addressLine1}</p>
                    {address.addressLine2 && <p style={{marginBottom: '4px'}}>{address.addressLine2}</p>}
                    <p>{address.city}, {address.state} {address.postalCode}</p>
                    <p>{address.country}</p>
                  </div>
                </div>
              </div>
            ))}
            
            <button
              onClick={() => setShowAddressForm(!showAddressForm)}
              style={{
                width: '100%',
                border: '2px dashed #d1d5db',
                borderRadius: '8px',
                padding: '16px',
                color: '#6b7280',
                backgroundColor: 'transparent',
                cursor: 'pointer',
                transition: 'all 0.2s ease',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center'
              }}
            >
              <Plus size={20} style={{marginBottom: '8px'}} />
              Add New Address
            </button>
          </div>

          {showAddressForm && (
            <form onSubmit={handleAddAddress} style={{
              marginTop: '24px',
              backgroundColor: '#f9fafb',
              padding: '24px',
              borderRadius: '8px'
            }}>
              <h4 style={{fontWeight: '600', color: '#1f2937', marginBottom: '16px'}}>Add New Address</h4>
              
              <div className="form-group">
                <label className="form-label">Address Line 1</label>
                <input
                  type="text"
                  value={newAddress.addressLine1}
                  onChange={(e) => setNewAddress({...newAddress, addressLine1: e.target.value})}
                  className="form-input"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Address Line 2</label>
                <input
                  type="text"
                  value={newAddress.addressLine2}
                  onChange={(e) => setNewAddress({...newAddress, addressLine2: e.target.value})}
                  className="form-input"
                />
              </div>

              <div className="grid grid-cols-2" style={{gap: '16px'}}>
                <div className="form-group">
                  <label className="form-label">City</label>
                  <input
                    type="text"
                    value={newAddress.city}
                    onChange={(e) => setNewAddress({...newAddress, city: e.target.value})}
                    className="form-input"
                    required
                  />
                </div>
                <div className="form-group">
                  <label className="form-label">State</label>
                  <input
                    type="text"
                    value={newAddress.state}
                    onChange={(e) => setNewAddress({...newAddress, state: e.target.value})}
                    className="form-input"
                    required
                  />
                </div>
              </div>

              <div className="grid grid-cols-2" style={{gap: '16px'}}>
                <div className="form-group">
                  <label className="form-label">Country</label>
                  <input
                    type="text"
                    value={newAddress.country}
                    onChange={(e) => setNewAddress({...newAddress, country: e.target.value})}
                    className="form-input"
                    required
                  />
                </div>
                <div className="form-group">
                  <label className="form-label">Postal Code</label>
                  <input
                    type="text"
                    value={newAddress.postalCode}
                    onChange={(e) => setNewAddress({...newAddress, postalCode: e.target.value})}
                    className="form-input"
                    required
                  />
                </div>
              </div>

              <div style={{display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '16px'}}>
                <input
                  type="checkbox"
                  checked={newAddress.default}
                  onChange={(e) => setNewAddress({...newAddress, default: e.target.checked})}
                  style={{accentColor: '#2563eb'}}
                />
                <label style={{fontSize: '14px', color: '#374151'}}>Set as default address</label>
              </div>

              <div style={{display: 'flex', gap: '12px'}}>
                <button type="submit" className="btn btn-primary">
                  Add Address
                </button>
                <button
                  type="button"
                  onClick={() => setShowAddressForm(false)}
                  className="btn btn-secondary"
                >
                  Cancel
                </button>
              </div>
            </form>
          )}
        </div>

        <div>
          <div className="order-summary">
            <h3 className="summary-title">Order Summary</h3>
            
            <div style={{marginBottom: '24px'}}>
              {cart.map((item) => (
                <div key={item.id} className="summary-line">
                  <span>{item.book.title} x {item.quantity}</span>
                  <span>${(item.book.price * item.quantity).toFixed(2)}</span>
                </div>
              ))}
              
              <div className="summary-total">
                <span>Total</span>
                <span>${getCartTotal()}</span>
              </div>
            </div>

            <button
              onClick={handlePlaceOrder}
              disabled={!selectedAddress}
              style={{
                width: '100%',
                backgroundColor: selectedAddress ? '#059669' : '#9ca3af',
                color: 'white',
                border: 'none',
                padding: '16px',
                borderRadius: '8px',
                fontSize: '16px',
                fontWeight: '600',
                cursor: selectedAddress ? 'pointer' : 'not-allowed',
                transition: 'background-color 0.2s ease',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '8px'
              }}
            >
              <CreditCard size={20} />
              Place Order
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CheckoutPage;