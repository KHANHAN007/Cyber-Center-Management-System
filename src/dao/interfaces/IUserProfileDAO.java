package dao.interfaces;

import model.UserProfile;

public interface IUserProfileDAO {
    UserProfile findByUserId(int userId);
    void create(UserProfile userProfile);
    void update(UserProfile userProfile);
}
