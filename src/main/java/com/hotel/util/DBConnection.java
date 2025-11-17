package com.hotel.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {

    private static final String PROPS_FILE = "/db.properties";
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = DBConnection.class.getResourceAsStream(PROPS_FILE)) {

            if (is == null) {
                throw new RuntimeException("Database properties file not found: " + PROPS_FILE);
            }

            PROPS.load(is);

            // Load driver
            String driver = PROPS.getProperty("db.driver");
            if (driver != null && !driver.isBlank()) {
                Class.forName(driver);
            }

        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to load database configuration: " + e.getMessage());
        }
    }

    private DBConnection() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password")
        );
    }
}
