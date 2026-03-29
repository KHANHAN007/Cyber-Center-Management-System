package dao.interfaces;

import model.User;
import model.UserProfile;

import java.util.List;

public interface IUserDAO {
    User findById(int userId);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    List<User> findAllActive();

    void create(User user);

    void update(User user);

    void delete(int userId);

    void softDelete(int userId);

    boolean usernameExists(String username);

    boolean emailExists(String email);

    UserProfile getUserWithProfile(int userId);

    User findUserByPhone(String phone);
}
