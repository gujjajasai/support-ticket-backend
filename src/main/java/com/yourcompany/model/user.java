package com.yourcompany.model;

/**
 * Represents a User entity, mirroring the structure of the 'Users' database table.
 * This class is a Plain Old Java Object (POJO) used to encapsulate user data
 * and transfer it between different layers of the application (e.g., from the DAO to a Servlet).
 */
public class user {

    // --- Private Fields ---
    // These fields directly correspond to the columns in the 'Users' table.
    // They are kept private to enforce encapsulation.

    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String role;

    // --- Constructors ---

    /**
     * Default no-argument constructor.
     * This is useful for frameworks and libraries that might need to instantiate
     * the object using reflection.
     */
    public user() {
    }

    /**
     * Constructor with all fields.
     * This is the primary constructor used for creating a new User object
     * with all its properties initialized at once.
     *
     * @param userId       The unique identifier for the user.
     * @param name         The full name of the user.
     * @param email        The user's unique email address.
     * @param passwordHash The securely hashed password.
     * @param role         The user's role (e.g., 'Customer' or 'Agent').
     */
    public user(int userId, String name, String email, String passwordHash, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // --- Getters and Setters ---
    // Public accessor (getters) and mutator (setters) methods provide controlled
    // access to the private fields of the class.

    public int getUserId() { return userId; }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}