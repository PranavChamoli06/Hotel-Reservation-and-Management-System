package com.hotel.migration;

import com.hotel.util.DBConnection;
import com.hotel.util.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PasswordMigration {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement("SELECT id, username, password FROM users");
             ResultSet rs = selectStmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String plainPassword = rs.getString("password");

                if (plainPassword != null && !plainPassword.isEmpty()) {
                    String salt = PasswordUtils.generateSalt();
                    String hash = PasswordUtils.hashPassword(plainPassword, salt);

                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE users SET salt = ?, password_hash = ? WHERE id = ?")) {
                        updateStmt.setString(1, salt);
                        updateStmt.setString(2, hash);
                        updateStmt.setInt(3, id);
                        updateStmt.executeUpdate();
                        System.out.println("✅ Updated user ID " + id);
                    }
                }
            }

            System.out.println("🎉 Migration completed!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
