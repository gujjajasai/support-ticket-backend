package com.yourcompany.model;

/**
 * Represents a Ticket entity, mirroring the structure of the 'Tickets' database table.
 * This class is a Plain Old Java Object (POJO) used to encapsulate ticket data
 * and transfer it between different layers of the application.
 */
public class Ticket {

    // --- Private Fields ---
    // These fields correspond directly to the columns in the 'Tickets' table.
    // Encapsulation is enforced by keeping them private.

    private int ticketId;
    private int customerId;
    private String title;
    private String description;
    private String status;
    private String priority;

    // --- Constructors ---

    /**
     * Default no-argument constructor.
     * This is useful for certain frameworks and libraries that might need to
     * instantiate the object via reflection before populating it.
     */
    public Ticket() {
    }

    /**
     * Constructor with all fields.
     * This is the primary constructor for creating a new Ticket object with all
     * its properties initialized at once.
     *
     * @param ticketId    The unique identifier for the ticket.
     * @param customerId  The ID of the customer who created the ticket.
     * @param title       The title of the ticket.
     * @param description The detailed description of the issue.
     * @param status      The current status of the ticket (e.g., 'Open', 'In Progress').
     * @param priority    The priority of the ticket (e.g., 'Low', 'Medium', 'High').
     */
    public Ticket(int ticketId, int customerId, String title, String description, String status, String priority) {
        this.ticketId = ticketId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    // --- Getters and Setters ---
    // Public accessor (getters) and mutator (setters) methods provide
    // controlled access to the private fields of the class.

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}