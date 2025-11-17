package com.hotel.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Reservation {
    private int id;
    private int userId;
    private String guestName;
    private int roomNumber;
    private String roomType;
    private Date checkIn;
    private Date checkOut;
    private String phoneNumber;
    private String status;
    private double totalPrice;
    private Timestamp createdAt;

    // Constructors
    public Reservation() {}

    public Reservation(int id, int userId, String guestName, int roomNumber, String roomType,
                       Date checkIn, Date checkOut, String phoneNumber,
                       String status, double totalPrice, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public Date getCheckIn() { return checkIn; }
    public void setCheckIn(Date checkIn) { this.checkIn = checkIn; }

    public Date getCheckOut() { return checkOut; }
    public void setCheckOut(Date checkOut) { this.checkOut = checkOut; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
