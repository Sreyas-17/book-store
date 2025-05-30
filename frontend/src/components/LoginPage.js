// src/components/LoginPage.js - Clean version without demo credentials
import React, { useState } from "react";
import { Book, Eye, EyeOff } from "lucide-react";
import { useAuth } from "../contexts/AuthContext";

const LoginPage = ({ setCurrentPage }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");

  const { handleLogin, loading } = useAuth();

  const onSubmit = async (e) => {
    e.preventDefault();
    setError("");

    console.log("LoginPage: Submitting login form for:", email);

    try {
      const result = await handleLogin(email, password);
      console.log("LoginPage: Login result:", result);

      if (result.success) {
        console.log("LoginPage: Login successful, user:", result.user);

        // Show role-specific welcome message
        if (result.user.role === 'ADMIN') {
          console.log("Admin logged in - will redirect to admin dashboard");
        } else if (result.user.role === 'VENDOR') {
          console.log("Vendor logged in - will redirect to vendor dashboard");
          if (!result.user.vendorApproved) {
            setError("Your vendor account is pending approval. You'll be notified once approved.");
            return;
          }
        } else {
          console.log("Regular user logged in - will redirect to home");
        }

        // Navigation will be handled by App.js useEffect based on role
      } else {
        const errorMessage = result.message || "Login failed. Please check your credentials.";
        console.log("LoginPage: Login failed:", errorMessage);
        setError(errorMessage);
      }
    } catch (error) {
      console.error("LoginPage: Login error:", error);
      setError("An unexpected error occurred. Please try again.");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <Book className="auth-icon" size={48} />
          <h1 className="auth-title">Welcome Back</h1>
          <p className="auth-subtitle">Sign in to your bookstore account</p>
        </div>

        <form onSubmit={onSubmit}>
          <div className="form-group">
            <label className="form-label">Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="form-input"
              placeholder="Enter your email"
              required
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label className="form-label">Password</label>
            <div style={{ position: 'relative' }}>
              <input
                type={showPassword ? "text" : "password"}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="form-input"
                placeholder="Enter your password"
                required
                disabled={loading}
                style={{ paddingRight: '50px' }}
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                style={{
                  position: 'absolute',
                  right: '12px',
                  top: '50%',
                  transform: 'translateY(-50%)',
                  background: 'none',
                  border: 'none',
                  cursor: 'pointer',
                  color: '#6b7280'
                }}
              >
                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
              </button>
            </div>
          </div>

          {error && <div className="alert alert-error">{error}</div>}

          <button
            type="submit"
            disabled={loading}
            className="btn btn-primary"
            style={{ width: "100%", padding: "12px" }}
          >
            {loading ? "Signing In..." : "Sign In"}
          </button>
        </form>

        {/* Registration Links */}
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          gap: '12px',
          marginTop: '20px',
          textAlign: 'center'
        }}>
          <div className="auth-link">
            <button
              onClick={() => setCurrentPage("register")}
              disabled={loading}
              style={{
                background: "none",
                border: "none",
                color: "#2563eb",
                cursor: "pointer",
                opacity: loading ? 0.5 : 1,
                fontSize: '14px'
              }}
            >
              Don't have an account? Sign up as Customer
            </button>
          </div>

          <div className="auth-link">
            <button
              onClick={() => setCurrentPage("vendor-register")}
              disabled={loading}
              style={{
                background: "none",
                border: "none",
                color: "#7c3aed",
                cursor: "pointer",
                opacity: loading ? 0.5 : 1,
                fontSize: '14px',
                fontWeight: '500'
              }}
            >
              Want to sell books? Register as Vendor →
            </button>
          </div>
        </div>

        {/* Forgot Password Link */}
        <div className="auth-link" style={{ marginTop: '16px' }}>
          <button
            onClick={() => {
              // You can implement forgot password functionality here
              alert('Forgot password functionality coming soon!');
            }}
            style={{
              background: "none",
              border: "none",
              color: "#6b7280",
              cursor: "pointer",
              fontSize: '13px',
              textDecoration: 'underline'
            }}
          >
            Forgot your password?
          </button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;