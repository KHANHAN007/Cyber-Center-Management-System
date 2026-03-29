package model;

public class LoyaltyPoints {
    private int loyaltyId;
    private int userId;
    private int points;

    public LoyaltyPoints() {
    }

    public LoyaltyPoints(int userId) {
        this.userId = userId;
        this.points = 0;
    }

    public int getLoyaltyId() {
        return loyaltyId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPoints() {
        return points;
    }

    public void setLoyaltyId(int loyaltyId) {
        this.loyaltyId = loyaltyId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "LoyaltyPoints{" +
                "loyaltyId=" + loyaltyId +
                ", userId=" + userId +
                ", points=" + points +
                '}';
    }
}
