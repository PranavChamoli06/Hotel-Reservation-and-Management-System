package com.hotel.dao;

import com.hotel.model.User;
import com.hotel.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class UserDAO {

    public UserDAO() {
        // ❗ NO CONNECTION CREATED HERE
        // controller can now initialize safely
    }

    public Optional<User> getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("role"),
                            rs.getString("salt"),
                            rs.getString("password_hash")
                    );

                    try { user.setLastLogin(rs.getTimestamp("last_login")); } catch (Exception ignored) {}
                    try { user.setLastLoginIp(rs.getString("last_login_ip")); } catch (Exception ignored) {}

                    return Optional.of(user);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, role, salt, password_hash) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getRole());
            stmt.setString(3, user.getSalt());
            stmt.setString(4, user.getPasswordHash());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void updateLoginActivity(int userId, String ip) {
        String sql = "UPDATE users SET last_login = NOW(), last_login_ip = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ip);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
