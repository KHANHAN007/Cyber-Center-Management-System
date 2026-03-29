package model;

import enums.UserStatus;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String userCode;
    private String username;
    private String password;
    private String email;
    private int roleId;
    private UserStatus status;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Role role;

    public User() {
    }

    public User(String username, String password, String email, int roleId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roleId = roleId;
        this.status = UserStatus.ACTIVE;
        this.isDeleted = false;
    }

    public User(int userId, String userCode, String username, String password,
                String email, int roleId, UserStatus status, boolean isDeleted,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.userCode = userCode;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roleId = roleId;
        this.status = status;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getRoleId() {
        return roleId;
    }

    public UserStatus getStatus() {
        return status;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Role getRole() {
        return role;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userCode='" + userCode + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roleId=" + roleId +
                ", status='" + status + '\'' +
                '}';
    }
}
