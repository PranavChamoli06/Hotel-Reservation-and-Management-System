package com.hotel.test;

import com.hotel.dao.ReservationDAO;
import com.hotel.model.Reservation;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for ReservationDAO using MySQL database.
 *
 * Requirements:
 *  - MySQL running locally
 *  - hotel_db_test schema available
 *  - testuser/testpassword user with privileges
 *  - 'users' table seeded with a user id=1
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationDAOTest {

    private static ReservationDAO dao;

    @BeforeAll
    public static void setup() {
        dao = new ReservationDAO();
        System.out.println("✅ Connected to MySQL test database successfully.");
    }

    @Test
    @Order(1)
    @DisplayName("Test Create, Read, Update, Delete Reservation")
    public void testCRUDOperations() {
        Date checkIn = Date.valueOf(LocalDate.now().plusDays(2));
        Date checkOut = Date.valueOf(LocalDate.now().plusDays(4));
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        // Create
        Reservation newRes = new Reservation(
                0,              // id = auto
                1,              // user_id = admin (seeded)
                "JUnit Tester", // guest_name
                120,            // room_number
                "Standard",     // room_type
                checkIn,
                checkOut,
                "9999999999",
                "Pending",
                5000.0,
                createdAt
        );

        boolean created = dao.createReservation(newRes);
        assertTrue(created, "Reservation should be created successfully");

        // Read
        var all = dao.getAllReservations();
        Optional<Reservation> match = all.stream()
                .filter(r -> "JUnit Tester".equals(r.getGuestName()))
                .findFirst();
        assertTrue(match.isPresent(), "Reservation must exist after creation");

        Reservation fetched = match.get();
        System.out.println("✅ Created reservation ID: " + fetched.getId());

        // Update
        fetched.setStatus("Confirmed");
        boolean updated = dao.updateReservation(fetched);
        assertTrue(updated, "Reservation should update successfully");

        Optional<Reservation> updatedFetched = dao.getReservationById(fetched.getId());
        assertTrue(updatedFetched.isPresent(), "Updated reservation should be found");
        assertEquals("Confirmed", updatedFetched.get().getStatus(), "Status must match after update");

        // Delete
        boolean deleted = dao.deleteReservation(fetched.getId());
        assertTrue(deleted, "Reservation should be deleted successfully");

        Optional<Reservation> deletedFetched = dao.getReservationById(fetched.getId());
        assertTrue(deletedFetched.isEmpty(), "Reservation should not exist after deletion");

        System.out.println("✅ CRUD test passed successfully for ReservationDAO.");
    }

    @Test
    @Order(2)
    @DisplayName("Test Room Availability Logic")
    public void testRoomAvailability() {
        Date in = Date.valueOf(LocalDate.now().plusDays(5));
        Date out = Date.valueOf(LocalDate.now().plusDays(6));

        boolean available = dao.isRoomAvailable(101, in, out);
        assertTrue(available, "Room 101 should be available for a future date range");

        System.out.println("✅ Room availability test passed.");
    }
}
