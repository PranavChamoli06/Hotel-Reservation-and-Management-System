package com.hotel.model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String role;
    private String salt;
    private String passwordHash;

    // activity
    private Timestamp lastLogin;
    private String lastLoginIp;

    public User() {}

    public User(int id, String username, String role, String salt, String passwordHash) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    // getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Timestamp getLastLogin() { return lastLogin; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }

    public String getLastLoginIp() { return lastLoginIp; }
    public void setLastLoginIp(String lastLoginIp) { this.lastLoginIp = lastLoginIp; }

    // convenience
    public void setLastLoginNow() { this.lastLogin = new Timestamp(System.currentTimeMillis()); }

    public void setLastLoginIpDirect(String ip) { this.lastLoginIp = ip; }
}
