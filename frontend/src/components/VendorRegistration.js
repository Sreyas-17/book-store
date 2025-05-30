// src/components/VendorRegistration.js
import React, { useState } from 'react';
import { Store } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { useVendor } from '../contexts/VendorContext';

const VendorRegistration = ({ setCurrentPage }) => {
  const { register, loading: authLoading } = useAuth();
  const { registerVendor, loading: vendorLoading } = useVendor();
  const [step, setStep] = useState(1);
  const [registeredUserId, setRegisteredUserId] = useState(null);
  const [userFormData, setUserFormData] = useState({
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    phone: ''
  });
  const [vendorFormData, setVendorFormData] = useState({
    businessName: '',
    businessEmail: '',
    businessPhone: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const loading = authLoading || vendorLoading;

  const handleUserFormSubmit = async (e) => {
    e.preventDefault();
    setError('');

    // Validate user form
    if (!userFormData.email || !userFormData.password || !userFormData.firstName || !userFormData.lastName) {
      setError('Please fill in all required fields');
      return;
    }

    if (userFormData.password.length < 6) {
      setError('Password must be at least 6 characters long');
      return;
    }

    try {
      console.log('📝 Registering user as VENDOR...');

      // Register user with VENDOR role using your existing authService
      const response = await register(userFormData, 'VENDOR');
      console.log('User registration response:', response);

      if (response.success) {
        console.log('✅ User registered successfully, moving to vendor registration...');
        setStep(2);
      } else {
        setError(response.message || 'User registration failed. Please try again.');
      }

    } catch (error) {
      console.error('💥 User registration error:', error);
      setError('Registration failed. Please try again.');
    }
  };

  const handleVendorFormSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    // Validate vendor form
    if (!vendorFormData.businessName || !vendorFormData.businessEmail || !vendorFormData.businessPhone) {
      setError('Please fill in all business information');
      return;
    }

    try {
      console.log('📝 Submitting vendor business information...');

      // Step 1: First login with the newly created user account to get userId
      console.log('🔐 Logging in with new user credentials...');
      const loginResponse = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: userFormData.email,
          password: userFormData.password
        })
      });

      const loginData = await loginResponse.json();
      console.log('Login response:', loginData);

      if (!loginData.success) {
        setError('Failed to authenticate. Please try logging in manually.');
        return;
      }

      // Step 2: Now register as vendor using the JWT token
      console.log('🏪 Registering vendor profile...');
      const vendorResponse = await fetch('http://localhost:8080/api/vendor/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${loginData.data.token}`
        },
        body: JSON.stringify(vendorFormData)
      });

      const vendorData = await vendorResponse.json();
      console.log('Vendor registration response:', vendorData);

      if (vendorData.success) {
        setSuccess('🎉 Vendor registration successful! Your account is pending admin approval. You will be notified once approved.');

        // Clear the temporary login (user will need to login again)
        localStorage.removeItem('token');
        localStorage.removeItem('user');

        // Redirect to login after a delay
        setTimeout(() => {
          setCurrentPage('login');
        }, 4000);
      } else {
        setError(vendorData.message || 'Vendor registration failed. Please try again.');
      }

    } catch (error) {
      console.error('💥 Vendor registration error:', error);
      setError('Registration failed. Please check your connection and try again.');
    }
  };

  const handleBack = () => {
    setStep(1);
    setError('');
  };

  return (
    <div className="auth-container" style={{background: 'linear-gradient(135deg, #f3e8ff 0%, #e9d5ff 100%)'}}>
      <div className="auth-card" style={{ maxWidth: '500px' }}>
        <div className="auth-header">
          <Store className="auth-icon" size={48} style={{color: '#7c3aed'}} />
          <h1 className="auth-title">Become a Vendor</h1>
          <p className="auth-subtitle">
            Join our marketplace and start selling your books
          </p>

          {/* Progress indicator */}
          <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            gap: '16px',
            marginTop: '20px',
            marginBottom: '20px'
          }}>
            <div style={{
              width: '32px',
              height: '32px',
              borderRadius: '50%',
              backgroundColor: '#7c3aed',
              color: 'white',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              fontSize: '14px',
              fontWeight: 'bold'
            }}>
              1
            </div>
            <div style={{
              width: '40px',
              height: '2px',
              backgroundColor: step >= 2 ? '#7c3aed' : '#d1d5db'
            }}></div>
            <div style={{
              width: '32px',
              height: '32px',
              borderRadius: '50%',
              backgroundColor: step >= 2 ? '#7c3aed' : '#d1d5db',
              color: 'white',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              fontSize: '14px',
              fontWeight: 'bold'
            }}>
              2
            </div>
          </div>

          <p style={{ fontSize: '14px', color: '#6b7280', textAlign: 'center' }}>
            Step {step} of 2: {step === 1 ? 'Personal Information' : 'Business Information'}
          </p>
        </div>

        {/* Step 1: Personal Information */}
        {step === 1 && (
          <form onSubmit={handleUserFormSubmit}>
            <h3 style={{ fontSize: '18px', fontWeight: '600', marginBottom: '16px', color: '#374151' }}>
              Personal Information
            </h3>

            <div style={{display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '20px'}}>
              <div>
                <label className="form-label">First Name *</label>
                <input
                  type="text"
                  value={userFormData.firstName}
                  onChange={(e) => setUserFormData({...userFormData, firstName: e.target.value})}
                  className="form-input"
                  placeholder="Enter first name"
                  required
                />
              </div>
              <div>
                <label className="form-label">Last Name *</label>
                <input
                  type="text"
                  value={userFormData.lastName}
                  onChange={(e) => setUserFormData({...userFormData, lastName: e.target.value})}
                  className="form-input"
                  placeholder="Enter last name"
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Email *</label>
              <input
                type="email"
                value={userFormData.email}
                onChange={(e) => setUserFormData({...userFormData, email: e.target.value})}
                className="form-input"
                placeholder="Enter your email"
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">Phone</label>
              <input
                type="tel"
                value={userFormData.phone}
                onChange={(e) => setUserFormData({...userFormData, phone: e.target.value})}
                className="form-input"
                placeholder="Enter your phone number"
              />
            </div>

            <div className="form-group">
              <label className="form-label">Password *</label>
              <input
                type="password"
                value={userFormData.password}
                onChange={(e) => setUserFormData({...userFormData, password: e.target.value})}
                className="form-input"
                placeholder="Create a password (min 6 characters)"
                required
                minLength="6"
              />
            </div>

            {error && (
              <div className="alert alert-error">
                {error}
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className="btn btn-primary"
              style={{
                width: '100%',
                padding: '14px',
                fontSize: '16px',
                backgroundColor: '#7c3aed',
                marginTop: '8px'
              }}
            >
              Continue to Business Information
            </button>
          </form>
        )}

        {/* Step 2: Business Information */}
        {step === 2 && (
          <form onSubmit={handleVendorFormSubmit}>
            <h3 style={{ fontSize: '18px', fontWeight: '600', marginBottom: '16px', color: '#374151' }}>
              Business Information
            </h3>

            <div className="form-group">
              <label className="form-label">Business Name *</label>
              <input
                type="text"
                value={vendorFormData.businessName}
                onChange={(e) => setVendorFormData({...vendorFormData, businessName: e.target.value})}
                className="form-input"
                placeholder="Enter your business name"
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">Business Email *</label>
              <input
                type="email"
                value={vendorFormData.businessEmail}
                onChange={(e) => setVendorFormData({...vendorFormData, businessEmail: e.target.value})}
                className="form-input"
                placeholder="Enter business email"
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">Business Phone *</label>
              <input
                type="tel"
                value={vendorFormData.businessPhone}
                onChange={(e) => setVendorFormData({...vendorFormData, businessPhone: e.target.value})}
                className="form-input"
                placeholder="Enter business phone number"
                required
              />
            </div>

            {error && (
              <div className="alert alert-error">
                {error}
              </div>
            )}

            {success && (
              <div className="alert alert-success">
                {success}
              </div>
            )}

            <div style={{ display: 'flex', gap: '12px', marginTop: '20px' }}>
              <button
                type="button"
                onClick={handleBack}
                className="btn btn-secondary"
                style={{ flex: '1' }}
              >
                Back
              </button>
              <button
                type="submit"
                disabled={loading}
                className="btn btn-primary"
                style={{
                  flex: '2',
                  padding: '14px',
                  fontSize: '16px',
                  backgroundColor: '#7c3aed'
                }}
              >
                {loading ? 'Submitting...' : 'Complete Registration'}
              </button>
            </div>
          </form>
        )}

        {/* Info Box */}
        <div style={{
          backgroundColor: '#f0f9ff',
          border: '1px solid #bae6fd',
          borderRadius: '8px',
          padding: '16px',
          marginTop: '24px'
        }}>
          <h4 style={{ fontSize: '14px', fontWeight: '600', color: '#0369a1', marginBottom: '8px' }}>
            📋 What happens next?
          </h4>
          <ul style={{ fontSize: '13px', color: '#075985', margin: 0, paddingLeft: '16px' }}>
            <li>Your vendor application will be reviewed by our admin team</li>
            <li>You'll receive an email notification once approved</li>
            <li>After approval, you can start adding and selling your books</li>
            <li>Review process typically takes 1-2 business days</li>
          </ul>
        </div>

        {/* Sign In Link */}
        <div className="auth-link">
          <span style={{color: '#6b7280'}}>Already have an account? </span>
          <button
            onClick={() => setCurrentPage('login')}
            style={{
              background: 'none',
              border: 'none',
              color: '#7c3aed',
              cursor: 'pointer',
              textDecoration: 'underline',
              fontSize: '14px'
            }}
          >
            Sign in
          </button>
        </div>
      </div>
    </div>
  );
};

export default VendorRegistration;