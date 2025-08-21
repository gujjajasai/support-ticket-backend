package com.yourcompany.service;
import com.yourcompany.model.*;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * A service class responsible for communicating with the external Node.js AI Agent microservice.
 * This class encapsulates the logic for making HTTP requests to the AI service,
 * sending ticket data for analysis, and receiving the AI's suggestions.
 */
public class AIAgentService {

    // The URL of the Node.js AI Agent microservice endpoint.
    // In a production environment, this should be loaded from a configuration file.
    private static final String AI_AGENT_URL = "http://localhost:3001/api/ai/analyze-ticket";

    /**
     * Sends a ticket's title and description to the AI Agent for analysis.
     *
     * @param ticket The Ticket object to be analyzed.
     * @return A JSON string containing the AI's analysis and suggestions, or null if an error occurs.
     */
    public String analyzeTicket(Ticket ticket) {
        HttpURLConnection connection = null;

        try {
            // Step 1: Create a JSON payload from the ticket object.
            // We create a temporary, anonymous object to ensure we only serialize the 'title' and 'description' fields,
            // as required by the AI Agent's API.
            String jsonPayload = new Gson().toJson(new Object() {
                final String title = ticket.getTitle();
                final String description = ticket.getDescription();
            });

            // Step 2: Create a URL object and open a connection.
            URL url = new URL(AI_AGENT_URL);
            connection = (HttpURLConnection) url.openConnection();

            // Step 3: Configure the HttpURLConnection for a POST request.
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8"); // Specify the content type as JSON.
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true); // This is crucial; it tells the connection that we will be sending a request body.

            // Step 4: Write the JSON payload to the request's output stream.
            // We use a try-with-resources block to ensure the OutputStream is automatically closed.
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Step 5: Read the response from the AI Agent's input stream.
            // We use a StringBuilder to efficiently build the response string.
            StringBuilder response = new StringBuilder();
            // A try-with-resources block ensures the BufferedReader is automatically closed.
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine())!= null) {
                    response.append(responseLine.trim());
                }
            }

            // Step 6: Return the complete response string.
            return response.toString();

        } catch (IOException e) {
            System.err.println("FAILURE: An error occurred while communicating with the AI Agent service.");
            e.printStackTrace();
            return null; // Return null to indicate that the operation failed.
        } finally {
            // Step 7: Disconnect the connection to release resources.
            if (connection!= null) {
                connection.disconnect();
            }
        }
    }
}