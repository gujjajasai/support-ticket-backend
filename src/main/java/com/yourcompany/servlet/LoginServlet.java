package com.yourcompany.servlet;
import com.yourcompany.dao.UserDAO;
import com.yourcompany.model.user;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;
import java.io.BufferedReader;
import com.yourcompany.model.LoginRequestDTO;
/**
 * A Java Servlet that handles user login requests.
 * Mapped to the '/api/auth/login' endpoint, this servlet processes POST requests
 * containing user credentials (email and password). It uses the UserDAO to
 * authenticate the user and manages the user's session upon successful login.
 */
@WebServlet("/api/auth/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    /**
     * The init() method is called by the servlet container once when the servlet is first loaded.
     * It's the perfect place for one-time initializations, such as creating our UserDAO instance.
     * This ensures that we use a single DAO object for the servlet's entire lifecycle.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    /**
     * Handles HTTP POST requests for user login.
     * It validates credentials and creates a session if they are correct.
     *
     * @param request  The HttpServletRequest object containing client request information.
     * @param response The HttpServletResponse object for sending the response to the client.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs during the request handling.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // --- NEW: Logic to read JSON from the request body ---
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // Use the Gson library to parse the JSON string into a User object
        Gson gson = new Gson();
        LoginRequestDTO loginRequest = gson.fromJson(sb.toString(), LoginRequestDTO.class);

        // Now we can get the email and password from our parsed object
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println("--- LOGIN ATTEMPT ---");
        System.out.println("Received Email: " + email);
        System.out.println("Received Plain Password: " + password);

        // --- End of new logic ---

        try {
            user dbUser = userDAO.getUserByEmail(email);

            if (dbUser != null && verifyPassword(password, dbUser.getPasswordHash())) {
                // ... the rest of your success logic can stay exactly the same ...
                System.out.println("User found in DB: " + dbUser.getEmail());
                System.out.println("Password from DB (Hashed): " + dbUser.getPasswordHash());



                HttpSession session = request.getSession(true);
                session.setAttribute("userId", dbUser.getUserId());
                session.setAttribute("role", dbUser.getRole());

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String jsonResponse = "{\"message\": \"Login successful\", \"role\": \"" + dbUser.getRole() + "\"}";
                response.getWriter().write(jsonResponse);

            } else {
                System.out.println("Passwords DO NOT MATCH!");
                sendUnauthorizedResponse(response);
            }
        } catch (Exception e) {
            // ... your error handling can stay the same ...
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // ... etc ...
            e.printStackTrace();
        }
    }
    /**
     * A helper method to send a 401 Unauthorized response.
     *
     * @param response The HttpServletResponse object.
     * @throws IOException If an I/O error occurs.
     */
    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonErrorResponse = "{\"error\": \"Invalid email or password\"}";
        response.getWriter().write(jsonErrorResponse);
    }

    /**
     * A simple placeholder method for password verification.
     * WARNING: This is NOT secure and is for demonstration purposes only.
     * In a production environment, use a library like BCrypt's checkpw() method
     * to securely compare a plain-text password with a hashed one.
     *
     * @param plainPassword The password provided by the user during login.
     * @param hashedPassword The hashed password retrieved from the database.
     * @return True if the passwords match, false otherwise.
     */
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        // This simulates checking the hash. In a real app, this would be a call
        // to a proper cryptographic library, e.g., BCrypt.checkpw(plainPassword, hashedPassword)
        return hashedPassword.equals(plainPassword + "-hashed");
    }
}