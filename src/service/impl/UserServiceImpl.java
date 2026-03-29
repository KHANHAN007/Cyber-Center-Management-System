package service.impl;

import dao.BaseDAO;
import dao.interfaces.*;
import enums.UserStatus;
import exception.BusinessException;
import exception.InvalidDataException;
import exception.NotFoundException;
import model.*;
import service.interfaces.IUserService;
import utils.PasswordHasher;

public class UserServiceImpl implements IUserService {
    private IUserDAO userDAO;
    private IUserProfileDAO userProfileDAO;
    private IWalletDAO walletDAO;
    private ILoyaltyPointsDAO loyaltyPointsDAO;
    private IRoleDAO roleDAO;

    public UserServiceImpl(IUserDAO userDAO, IUserProfileDAO userProfileDAO, IWalletDAO walletDAO,
            ILoyaltyPointsDAO loyaltyPointsDAO, IRoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.userProfileDAO = userProfileDAO;
        this.walletDAO = walletDAO;
        this.loyaltyPointsDAO = loyaltyPointsDAO;
        this.roleDAO = roleDAO;
    }

    @Override
    public User login(String username, String password) throws BusinessException {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new InvalidDataException("Username cannot be empty");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new InvalidDataException("Password cannot be empty");
            }

            User user = userDAO.findByUsername(username);
            if (user == null || !PasswordHasher.verifyPassword(password, user.getPassword())) {
                throw new BusinessException("Invalid username or password");
            }

            if (user.getStatus() != enums.UserStatus.ACTIVE) {
                throw new BusinessException("User account is not active");
            }

            Role role = roleDAO.findById(user.getRoleId());
            user.setRole(role);
            return user;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("An error occurred during login: " + e.getMessage(), e);
        }
    }

    @Override
    public User register(String username, String password, String email, String fullName, String phone, String address,
            String referralPhone)
            throws BusinessException {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new InvalidDataException("Username cannot be empty");
            }

            if (password == null || password.trim().isEmpty()) {
                throw new InvalidDataException("Password cannot be empty");
            }

            if (email == null || email.trim().isEmpty()) {
                throw new InvalidDataException("Email cannot be empty");
            }

            if (fullName == null || fullName.trim().isEmpty()) {
                throw new InvalidDataException("Full name cannot be empty");
            }

            if (userDAO.usernameExists(username)) {
                throw new BusinessException("Username already exists");
            }

            if (userDAO.emailExists(email)) {
                throw new BusinessException("Email already exists");
            }

            String hashedPassword = PasswordHasher.hashPassword(password);

            Role customerRole = roleDAO.findByCode("CU001");
            System.out.println("Customer role: " + customerRole);
            if (customerRole == null) {
                throw new BusinessException("Customer role not found in system. Please contact administrator.");
            }

            User user = new User(username, hashedPassword, email, customerRole.getRoleId());
            user.setStatus(UserStatus.ACTIVE);
            userDAO.create(user);

            User newUser = userDAO.findByUsername(username);
            if (newUser == null) {
                throw new BusinessException("Failed to create user");
            }

            UserProfile profile = new UserProfile(newUser.getUserId(), fullName, phone, address);
            userProfileDAO.create(profile);

            Wallet wallet = new Wallet(newUser.getUserId());
            wallet.setBalance(0);
            walletDAO.create(wallet);

            LoyaltyPoints loyaltyPoints = new LoyaltyPoints(newUser.getUserId());
            loyaltyPoints.setLoyaltyId(0);
            loyaltyPointsDAO.create(loyaltyPoints);

            // Handle referral bonus
            if (referralPhone != null && !referralPhone.trim().isEmpty()) {
                User referrerUser = userDAO.findUserByPhone(referralPhone.trim());
                if (referrerUser != null) {
                    // Add 200 points to both new user and referrer
                    loyaltyPointsDAO.addPoints(newUser.getUserId(), 200);
                    loyaltyPointsDAO.addPoints(referrerUser.getUserId(), 200);
                }
            }

            return newUser;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("An error occurred during registration: " + e.getMessage(), e);
        }
    }

    @Override
    public User getUserById(int userId) throws BusinessException {
        try {
            if (userId <= 0) {
                throw new InvalidDataException("Invalid user ID");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                throw new NotFoundException("User not found");
            }

            Role role = roleDAO.findById(user.getRoleId());
            user.setRole(role);
            return user;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("An error occurred while retrieving user: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateUser(User user) throws BusinessException {
        try {
            if (user == null || user.getUserId() <= 0) {
                throw new InvalidDataException("Invalid user data");
            }
            User existingUser = userDAO.findById(user.getUserId());
            if (existingUser == null) {
                throw new NotFoundException("User not found");
            }
            userDAO.update(user);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("An error occurred while updating user: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(int userId) {
        try {
            if (userId <= 0) {
                throw new InvalidDataException("ID người dùng không hợp lệ");
            }

            User user = userDAO.findById(userId);
            if (user == null) {
                throw new NotFoundException("Người dùng không tồn tại");
            }
            userDAO.softDelete(userId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("An error occurred while deleting user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && user.getRole() != null &&
                user.getRole().getRoleName().equals("ADMIN");
    }

    @Override
    public boolean isStaff(User user) {
        return user != null && user.getRole() != null &&
                user.getRole().getRoleName().equals("STAFF");
    }

    @Override
    public boolean isCustomer(User user) {
        return user != null && user.getRole() != null &&
                user.getRole().getRoleName().equals("CUSTOMER");
    }

    @Override
    public boolean usernameExists(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new InvalidDataException("Username cannot be empty");
            }
            return userDAO.usernameExists(username);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("An error occurred while checking username: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean emailExists(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new InvalidDataException("Email cannot be empty");
            }
            return userDAO.emailExists(email);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("An error occurred while checking email: " + e.getMessage(), e);
        }
    }
}
