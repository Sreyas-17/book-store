import React, { useState } from 'react';
import { User, ShoppingCart, Heart, MapPin } from 'lucide-react';
import { apiCall } from '../utils/api';

const ProfilePage = ({ 
  user, 
  setUser, 
  token, 
  orders, 
  wishlist, 
  addresses, 
  setCurrentPage 
}) => {
  const [isEditing, setIsEditing] = useState(false);
  const [profileData, setProfileData] = useState({
    firstName: user?.firstName || '',
    lastName: user?.lastName || '',
    phone: user?.phone || '',
    email: user?.email || ''
  });

  const handleUpdateProfile = async (e) => {
    e.preventDefault();
    const response = await apiCall(`/auth/profile/${user.id}`, {
      method: 'PUT',
      body: JSON.stringify(profileData),
    }, token);
    
    if (response.success) {
      setUser(response.data);
      setIsEditing(false);
    }
  };

  if (!user) {
    return (
      <div className="empty-state">
        <User className="empty-icon" size={64} />
        <h2 className="empty-title">Please sign in to view your profile</h2>
        <button
          onClick={() => setCurrentPage('login')}
          className="btn btn-primary"
        >
          Sign In
        </button>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="page-header">
        <h2 className="page-title">My Profile</h2>
      </div>
      
      <div className="grid grid-cols-2" style={{gap: '32px'}}>
        <div className="card" style={{padding: '24px'}}>
          <div className="flex justify-between items-center mb-6">
            <h3 style={{fontSize: '20px', fontWeight: 'bold', color: '#1f2937'}}>Personal Information</h3>
            <button
              onClick={() => setIsEditing(!isEditing)}
              style={{color: '#2563eb', background: 'none', border: 'none', cursor: 'pointer', fontWeight: '500'}}
            >
              {isEditing ? 'Cancel' : 'Edit'}
            </button>
          </div>

          {isEditing ? (
            <form onSubmit={handleUpdateProfile} className="space-y-4">
              <div className="grid grid-cols-2" style={{gap: '16px'}}>
                <div className="form-group">
                  <label className="form-label">First Name</label>
                  <input
                    type="text"
                    value={profileData.firstName}
                    onChange={(e) => setProfileData({...profileData, firstName: e.target.value})}
                    className="form-input"
                    required
                  />
                </div>
                <div className="form-group">
                  <label className="form-label">Last Name</label>
                  <input
                    type="text"
                    value={profileData.lastName}
                    onChange={(e) => setProfileData({...profileData, lastName: e.target.value})}
                    className="form-input"
                    required
                  />
                </div>
              </div>

              <div className="form-group">
                <label className="form-label">Email</label>
                <input
                  type="email"
                  value={profileData.email}
                  onChange={(e) => setProfileData({...profileData, email: e.target.value})}
                  className="form-input"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Phone</label>
                <input
                  type="tel"
                  value={profileData.phone}
                  onChange={(e) => setProfileData({...profileData, phone: e.target.value})}
                  className="form-input"
                />
              </div>

              <button
                type="submit"
                className="btn btn-primary"
                style={{width: '100%'}}
              >
                Update Profile
              </button>
            </form>
          ) : (
            <div className="space-y-4">
              <div>
                <label style={{display: 'block', fontSize: '14px', fontWeight: '500', color: '#6b7280'}}>Name</label>
                <p style={{fontSize: '18px', color: '#1f2937'}}>{user.firstName} {user.lastName}</p>
              </div>
              <div>
                <label style={{display: 'block', fontSize: '14px', fontWeight: '500', color: '#6b7280'}}>Email</label>
                <p style={{fontSize: '18px', color: '#1f2937'}}>{user.email}</p>
              </div>
              <div>
                <label style={{display: 'block', fontSize: '14px', fontWeight: '500', color: '#6b7280'}}>Phone</label>
                <p style={{fontSize: '18px', color: '#1f2937'}}>{user.phone || 'Not provided'}</p>
              </div>
              <div>
                <label style={{display: 'block', fontSize: '14px', fontWeight: '500', color: '#6b7280'}}>Member Since</label>
                <p style={{fontSize: '18px', color: '#1f2937'}}>
                  {new Date(user.createdAt).toLocaleDateString()}
                </p>
              </div>
            </div>
          )}
        </div>

        <div className="card" style={{padding: '24px'}}>
          <h3 style={{fontSize: '20px', fontWeight: 'bold', color: '#1f2937', marginBottom: '24px'}}>Quick Stats</h3>
          
          <div className="space-y-4">
            <div style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              padding: '16px',
              backgroundColor: '#eff6ff',
              borderRadius: '8px'
            }}>
              <div>
                <p style={{fontSize: '14px', color: '#6b7280'}}>Total Orders</p>
                <p style={{fontSize: '24px', fontWeight: 'bold', color: '#2563eb'}}>{orders.length}</p>
              </div>
              <ShoppingCart size={32} style={{color: '#2563eb'}} />
            </div>

            <div style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              padding: '16px',
              backgroundColor: '#fef2f2',
              borderRadius: '8px'
            }}>
              <div>
                <p style={{fontSize: '14px', color: '#6b7280'}}>Wishlist Items</p>
                <p style={{fontSize: '24px', fontWeight: 'bold', color: '#dc2626'}}>{wishlist.length}</p>
              </div>
              <Heart size={32} style={{color: '#dc2626'}} />
            </div>

            <div style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              padding: '16px',
              backgroundColor: '#f9fafb',
              borderRadius: '8px'
            }}>
              <div>
                <p style={{fontSize: '14px', color: '#6b7280'}}>Saved Addresses</p>
                <p style={{fontSize: '24px', fontWeight: 'bold', color: '#6b7280'}}>{addresses.length}</p>
              </div>
              <MapPin size={32} style={{color: '#6b7280'}} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;