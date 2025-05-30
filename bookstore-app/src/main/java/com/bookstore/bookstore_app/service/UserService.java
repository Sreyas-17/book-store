package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.dto.LoginRequest;
import com.bookstore.bookstore_app.dto.LoginResponse;
import com.bookstore.bookstore_app.dto.RegisterRequest;
import com.bookstore.bookstore_app.entity.User;
import com.bookstore.bookstore_app.entity.Vendor;
import com.bookstore.bookstore_app.repository.UserRepository;
import com.bookstore.bookstore_app.repository.VendorRepository;
import com.bookstore.bookstore_app.config.JwtUtil;
import com.bookstore.bookstore_app.exception.BusinessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        logger.info("Attempting to register user with email: {}", request.getEmail());

        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                logger.warn("Registration failed - Email already exists: {}", request.getEmail());
                throw new BusinessException("Email already exists");
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPhone(request.getPhone());

            User savedUser = userRepository.save(user);
            logger.info("User registered successfully with ID: {} and email: {}", savedUser.getId(), savedUser.getEmail());

            return "User registered successfully";

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error during user registration for email: {}", request.getEmail(), ex);
            throw new RuntimeException("Registration failed due to server error");
        }
    }

    public String login(LoginRequest request) {
        logger.info("Login attempt for email: {}", request.getEmail());

        try {
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
            if (userOpt.isEmpty()) {
                logger.warn("Login failed - User not found: {}", request.getEmail());
                throw new BusinessException("Invalid credentials");
            }

            User user = userOpt.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                logger.warn("Login failed - Invalid password for user: {}", request.getEmail());
                throw new BusinessException("Invalid credentials");
            }

            String token = jwtUtil.generateToken(user.getEmail());
            logger.info("Login successful for user: {} with ID: {}", user.getEmail(), user.getId());

            return token;

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error during login for email: {}", request.getEmail(), ex);
            throw new RuntimeException("Login failed due to server error");
        }
    }

    public User getUserByEmail(String email) {
        logger.debug("Fetching user by email: {}", email);

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.warn("User not found with email: {}", email);
                        return new BusinessException("User not found");
                    });

            logger.debug("User found with email: {} and ID: {}", email, user.getId());
            return user;

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error fetching user by email: {}", email, ex);
            throw new RuntimeException("Failed to fetch user");
        }
    }

    public User updateUser(Long userId, User updatedUser) {
        logger.info("Updating user with ID: {}", userId);

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.warn("User not found for update with ID: {}", userId);
                        return new BusinessException("User not found");
                    });

            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setPhone(updatedUser.getPhone());

            User savedUser = userRepository.save(user);
            logger.info("User updated successfully with ID: {}", savedUser.getId());

            return savedUser;

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error updating user with ID: {}", userId, ex);
            throw new RuntimeException("Failed to update user");
        }
    }

    public String resetPassword(String email) {
        logger.info("Password reset requested for email: {}", email);

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.warn("Password reset failed - User not found: {}", email);
                        return new BusinessException("User not found");
                    });

            String tempPassword = "temp123456";
            user.setPassword(passwordEncoder.encode(tempPassword));
            userRepository.save(user);

            logger.info("Password reset successful for user: {}", email);
            return "Password reset request processed. Contact admin for new password.";

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error during password reset for email: {}", email, ex);
            throw new RuntimeException("Password reset failed");
        }
    }

    public String verifyUser(Long userId) {
        logger.info("User verification requested for ID: {}", userId);

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.warn("User verification failed - User not found with ID: {}", userId);
                        return new BusinessException("User not found");
                    });

            user.setVerified(true);
            userRepository.save(user);

            logger.info("User verified successfully with ID: {}", userId);
            return "User verified successfully";

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error during user verification for ID: {}", userId, ex);
            throw new RuntimeException("User verification failed");
        }
    }

    public LoginResponse loginWithRoleInfo(LoginRequest request) {
        logger.info("Login attempt with role info for email: {}", request.getEmail());

        try {
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
            if (userOpt.isEmpty()) {
                logger.warn("Login failed - User not found: {}", request.getEmail());
                throw new BusinessException("Invalid credentials");
            }

            User user = userOpt.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                logger.warn("Login failed - Invalid password for user: {}", request.getEmail());
                throw new BusinessException("Invalid credentials");
            }

            logger.info("Generating token for user: {} with role: {} and ID: {}",
                    user.getEmail(), user.getRole(), user.getId());
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());

            LoginResponse response;

            // If user is a vendor, include vendor information
            if (user.getRole() == User.Role.VENDOR) {
                Optional<Vendor> vendorOpt = vendorRepository.findByUserId(user.getId());
                if (vendorOpt.isPresent()) {
                    Vendor vendor = vendorOpt.get();
                    response = LoginResponse.forVendor(token, user, vendor.getId(),
                            vendor.getBusinessName(), vendor.isApproved());
                } else {
                    response = new LoginResponse(token, user);
                }
            } else {
                response = new LoginResponse(token, user);
            }

            logger.info("Login successful for {} with role: {}", user.getEmail(), user.getRole());
            return response;

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error during role-based login for email: {}", request.getEmail(), ex);
            throw new RuntimeException("Login failed due to server error");
        }
    }

    public String registerWithRole(RegisterRequest request, User.Role role) {
        logger.info("Attempting to register user with email: {} and role: {}", request.getEmail(), role);

        try {
            // Check if user already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                logger.warn("Registration failed - Email already exists: {}", request.getEmail());
                throw new BusinessException("Email already exists");
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPhone(request.getPhone());
            user.setRole(role);

            User savedUser = userRepository.save(user);
            logger.info("User registered successfully with ID: {}, email: {}, and role: {}",
                    savedUser.getId(), savedUser.getEmail(), savedUser.getRole());

            return "User registered successfully with role: " + role;

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error during user registration for email: {}", request.getEmail(), ex);
            throw new RuntimeException("Registration failed due to server error");
        }
    }
}