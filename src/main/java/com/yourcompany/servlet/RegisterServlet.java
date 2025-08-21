package com.yourcompany.servlet;

// Corrected import with capital 'U'
import com.yourcompany.model.user;
import com.yourcompany.dao.UserDAO;

import java.io.IOException;
import java.sql.SQLException; // Important to import SQLException
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.yourcompany.model.RegisterRequestDTO; // Assuming you have this DTO
import java.io.BufferedReader;

@WebServlet("/api/auth/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read JSON from request body
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        Gson gson = new Gson();
        RegisterRequestDTO registerRequest = gson.fromJson(sb.toString(), RegisterRequestDTO.class);

        String passwordHash = hashPassword(registerRequest.getPassword());

        // Corrected with capital 'U' for the User class
        user newUser = new user();
        newUser.setName(registerRequest.getName());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPasswordHash(passwordHash);
        newUser.setRole(registerRequest.getRole());

        try {
            // This is the important part
            userDAO.registerUser(newUser);

            // This code will ONLY run if the line above succeeds
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"User registered successfully\"}");

        } catch (SQLException e) {
            // This block will now correctly catch the database failure
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict is a good code for a duplicate email
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Registration failed. The email may already be in use.\"}");
            System.err.println("--- REGISTRATION FAILED ---");
            e.printStackTrace(); // Log the real error to the server console
        } catch (Exception e) {
            // Catch any other unexpected errors
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"An unexpected server error occurred.\"}");
            System.err.println("--- UNEXPECTED REGISTRATION ERROR ---");
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            return null;
        }
        return password + "-hashed";
    }
}