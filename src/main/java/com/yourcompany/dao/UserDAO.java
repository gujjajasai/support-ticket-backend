package com.yourcompany.dao;

import com.yourcompany.model.user;
import com.yourcompany.DatabaseConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void registerUser(user user) throws SQLException {
        String sql = "INSERT INTO Users (name, email, password_hash, role) VALUES (?, ?, ?, ?)";

        // try-with-resources automatically closes the connection and statement
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPasswordHash());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.executeUpdate();
            System.out.println("DAO: User registered successfully.");
        }
        // No finally block needed for closing resources, it's automatic!
    }

    public user getUserByEmail(String email) throws SQLException {
        user user = null;
        String sql = "SELECT user_id, name, email, password_hash, role FROM Users WHERE email = ?";

        // try-with-resources automatically closes the connection, statement, and result set
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    user = new user();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setRole(rs.getString("role"));
                }
            }
        }
        return user;
    }
}