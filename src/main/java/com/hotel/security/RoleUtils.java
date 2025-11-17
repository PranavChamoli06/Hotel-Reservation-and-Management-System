package com.hotel.security;

public class RoleUtils {

    public static boolean isAdmin(String role) {
        return "admin".equalsIgnoreCase(role);
    }

    public static boolean isManager(String role) {
        return "manager".equalsIgnoreCase(role);
    }

    public static boolean isStaff(String role) {
        return "staff".equalsIgnoreCase(role);
    }

    public static boolean isUser(String role) {
        return "user".equalsIgnoreCase(role);
    }

    public static boolean canCreateReservation(String role) {
        return isAdmin(role) || isManager(role) || isStaff(role);
    }

    public static boolean canEditReservation(String role) {
        return isAdmin(role) || isManager(role);
    }

    public static boolean canDeleteReservation(String role) {
        return isAdmin(role);
    }
}
