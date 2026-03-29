package model;

public class UserProfile {
    private int profileId;
    private int userId;
    private String fullName;
    private String phone;
    private String address;
    private int loyaltyPoints;

    public UserProfile() {
    }

    public UserProfile(int userId, String fullName, String phone, String address) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
    }

    public int getProfileId() {
        return profileId;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "profileId=" + profileId +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", loyaltyPoints=" + loyaltyPoints +
                '}';
    }
}
