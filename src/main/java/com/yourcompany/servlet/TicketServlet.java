package com.yourcompany.servlet;
import com.yourcompany.model.Ticket;
import com.yourcompany.dao.TicketDAO;
import com.yourcompany.service.AIAgentService;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Java Servlet to handle all API requests for tickets at the '/api/tickets' endpoint.
 * It handles GET requests to fetch all tickets and POST requests to create a new ticket.
 * This servlet demonstrates the controller layer's role in orchestrating calls to the
 * data access layer (TicketDAO) and other services (AIAgentService).
 */
@WebServlet("/api/tickets")
public class TicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TicketDAO ticketDAO;
    private AIAgentService aiAgentService;

    /**
     * The init() method is called by the servlet container once when the servlet is first loaded.
     * We initialize our DAO and the new AIAgentService here.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        ticketDAO = new TicketDAO();
        aiAgentService = new AIAgentService();
    }

    /**
     * Handles HTTP GET requests to fetch all tickets.
     * It retrieves the list of tickets from the DAO and uses the Gson library
     * to serialize them into a JSON array string.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Ticket> tickets = ticketDAO.getAllTickets();
            String jsonResponse = new Gson().toJson(tickets);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonErrorResponse = "{\"error\": \"Failed to retrieve tickets.\"}";
            response.getWriter().write(jsonErrorResponse);
            e.printStackTrace();
        }
    }

    /**
     * Handles HTTP POST requests to create a new ticket.
     * It reads a JSON payload, saves the ticket to the database, and then calls
     * an external AI service for analysis.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Step 1: Read the JSON request body.
            // A StringBuilder is used to efficiently construct the string from the request's input stream.
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine())!= null) {
                    sb.append(line);
                }
            }
            String requestBody = sb.toString();

            // Step 2: Parse the JSON string into a Ticket object using Gson.
            // Gson simplifies the conversion from a JSON string to a Java object.
            Gson gson = new Gson();
            Ticket newTicket = gson.fromJson(requestBody, Ticket.class);

            // Step 3: Call the DAO to save the new ticket to the database.
            // This persists the ticket before any further processing.
            ticketDAO.createTicket(newTicket);

            // Step 4: Immediately after saving, send the ticket data to the AI Agent for analysis.
            // This demonstrates a microservice architecture where one service calls another for specialized processing.
            String aiAgentResponse = aiAgentService.analyzeTicket(newTicket);

            // Step 5: Log the AI's response to the server console.
            // In a real application, this response would be parsed and used to update the ticket
            // (e.g., set priority, add tags, or create a suggested reply).
            System.out.println("AI Agent Response: " + aiAgentResponse);

            // Step 6: Send a success response back to the client.
            // A 201 Created status code is the standard and correct response for a successful POST request.
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonSuccessResponse = "{\"message\": \"Ticket created successfully and sent for analysis.\"}";
            response.getWriter().write(jsonSuccessResponse);

        } catch (Exception e) {
            // Handle potential errors, such as JSON parsing issues or database connection problems.
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonErrorResponse = "{\"error\": \"Failed to create ticket.\"}";
            response.getWriter().write(jsonErrorResponse);
            e.printStackTrace(); // Log the full stack trace for debugging purposes.
        }
    }
}