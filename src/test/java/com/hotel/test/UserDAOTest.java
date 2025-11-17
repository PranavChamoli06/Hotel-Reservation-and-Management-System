package com.hotel.test;

import com.hotel.dao.UserDAO;
import com.hotel.model.User;
import com.hotel.util.DBConnection;
import com.hotel.util.PasswordUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for UserDAO using the MySQL test database.
 *
 * Requirements:
 * - src/test/resources/db.properties must point to hotel_db_test (test DB)
 * - The test DB must be created (see previous SQL you ran)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTest {

    private static final String TEST_USERNAME = "dao_test_user";
    private static final String TEST_PASSWORD = "pass12345"; // plaintext used only for test
    private static UserDAO userDAO;

    @BeforeAll
    public static void init() throws Exception {
        userDAO = new UserDAO();

        // ensure test user is present with salt+hash
        String salt = PasswordUtils.generateSalt();
        String hash = PasswordUtils.hashPassword(TEST_PASSWORD, salt);

        try (Connection conn = DBConnection.getConnection()) {
            // remove existing test user if found
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM users WHERE username = ?")) {
                del.setString(1, TEST_USERNAME);
                del.executeUpdate();
            }

            // insert fresh test user
            try (PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO users (username, role, salt, password_hash) VALUES (?, 'staff', ?, ?)")) {
                ins.setString(1, TEST_USERNAME);
                ins.setString(2, salt);
                ins.setString(3, hash);
                ins.executeUpdate();
            }
        }
    }

    @AfterAll
    public static void cleanup() throws Exception {
        // remove the test user
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement del = conn.prepareStatement("DELETE FROM users WHERE username = ?")) {
            del.setString(1, TEST_USERNAME);
            del.executeUpdate();
        }
    }

    @Test
    @Order(1)
    @DisplayName("getUserByUsername should return the inserted user with salt & hash")
    public void testGetUserByUsername() {
        Optional<User> maybe = userDAO.getUserByUsername(TEST_USERNAME);
        assertTrue(maybe.isPresent(), "User should be present");
        User u = maybe.get();

        assertEquals(TEST_USERNAME, u.getUsername());
        assertNotNull(u.getSalt(), "Salt should be stored");
        assertNotNull(u.getPasswordHash(), "Password hash should be stored");
        assertEquals("staff", u.getRole(), "Role should be 'staff' by default");
    }

    @Test
    @Order(2)
    @DisplayName("Stored hash should match hash of the known plaintext + salt")
    public void testPasswordHashingMatches() {
        Optional<User> maybe = userDAO.getUserByUsername(TEST_USERNAME);
        assertTrue(maybe.isPresent());
        User u = maybe.get();

        String recomputed = PasswordUtils.hashPassword(TEST_PASSWORD, u.getSalt());
        assertEquals(u.getPasswordHash(), recomputed, "Recomputed hash must match stored hash");
    }

    @Test
    @Order(3)
    @DisplayName("Wrong password must not match stored hash")
    public void testWrongPasswordFails() {
        Optional<User> maybe = userDAO.getUserByUsername(TEST_USERNAME);
        assertTrue(maybe.isPresent());
        User u = maybe.get();

        String wrongHash = PasswordUtils.hashPassword("wrong-password-xyz", u.getSalt());
        assertNotEquals(u.getPasswordHash(), wrongHash, "Wrong password should not match the stored hash");
    }
}
