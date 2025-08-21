package com.yourcompany.dao;

import com.yourcompany.model.Ticket;
import com.yourcompany.DatabaseConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public void createTicket(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO Tickets (title, description, customer_id) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, ticket.getTitle());
            preparedStatement.setString(2, ticket.getDescription());
            preparedStatement.setInt(3, ticket.getCustomerId()); // Assuming IDs are back to int
            preparedStatement.executeUpdate();
            System.out.println("DAO: Ticket created successfully.");
        }
    }

    public List<Ticket> getAllTickets() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT ticket_id, customer_id, title, description, status, priority FROM Tickets ORDER BY created_at DESC";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setCustomerId(rs.getInt("customer_id")); // Assuming IDs are back to int
                ticket.setTitle(rs.getString("title"));
                ticket.setDescription(rs.getString("description"));
                ticket.setStatus(rs.getString("status"));
                ticket.setPriority(rs.getString("priority"));
                tickets.add(ticket);
            }
        }
        return tickets;
    }
}