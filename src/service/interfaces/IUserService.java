package service.interfaces;

import model.User;

public interface IUserService {
    User login(String username, String password);

    User register(String username, String password, String email, String fullName, String phone, String address,
            String referralPhone);

    User getUserById(int userId);

    void updateUser(User user);

    void deleteUser(int userId);

    boolean isAdmin(User user);

    boolean isStaff(User user);

    boolean isCustomer(User user);

    boolean usernameExists(String username);

    boolean emailExists(String email);
}
