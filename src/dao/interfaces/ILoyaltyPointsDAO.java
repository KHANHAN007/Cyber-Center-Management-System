package dao.interfaces;

import model.LoyaltyPoints;

public interface ILoyaltyPointsDAO {
    LoyaltyPoints findByUserId(int userId);
    void create(LoyaltyPoints loyaltyPoints);
    void addPoints(int userId, int points);
    void deductPoints(int userId, int points);
    int getPoints(int userId);
}
