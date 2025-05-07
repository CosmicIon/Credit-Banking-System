package com.creditbanking.model;

import java.io.Serializable;
import java.util.Scanner;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String role;            // "ADMIN" or "CUSTOMER"
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private boolean isActive;

    // Constructor to create a User from user input
    public User() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        this.username = scanner.nextLine();

        System.out.print("Enter password: ");
        this.password = scanner.nextLine();

        System.out.print("Enter role (ADMIN/CUSTOMER): ");
        this.role = scanner.nextLine();

        System.out.print("Enter full name: ");
        this.fullName = scanner.nextLine();

        System.out.print("Enter email: ");
        this.email = scanner.nextLine();

        System.out.print("Enter phone number: ");
        this.phoneNumber = scanner.nextLine();

        System.out.print("Enter address: ");
        this.address = scanner.nextLine();

        this.isActive = true; // default to active
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Optional: toString for logging/debugging (password hidden)
    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", role='" + role + '\'' +
               ", fullName='" + fullName + '\'' +
               ", email='" + email + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", address='" + address + '\'' +
               ", isActive=" + isActive +
               '}';
    }

    // Main method to test user input
    public static void main(String[] args) {
        // Creating a new User object which will prompt for input
        User newUser = new User();  // This will prompt the user for input

        // Printing the details of the User object
        System.out.println("\nUser created: ");
        System.out.println(newUser);  // This will print the details of the user
    }
}