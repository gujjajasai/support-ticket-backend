package com.yourcompany.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A simple CORS filter to allow cross-origin requests from our frontend.
 * This is essential for allowing our Vercel-hosted React app to communicate
 * with our Render-hosted Java backend.
 */
@WebFilter("/*") // This filter applies to all requests to our application
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // --- THIS IS THE CRITICAL PART ---
        // Set the Access-Control-Allow-Origin header to your Vercel frontend URL.
        // Be sure to replace the placeholder with your actual URL.
        response.setHeader("Access-Control-Allow-Origin", "https://support-ticket-frontend-gujjajasai.vercel.app"); // <-- IMPORTANT

        // Set other necessary CORS headers
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true"); // Important for sessions/cookies

        // For preflight (OPTIONS) requests, we just send a 200 OK and stop processing.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;

        }

        // For all other requests, continue the filter chain to the actual servlet.
        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if any.
    }

    @Override
    public void destroy() {
        // Cleanup code, if any.
    }
}