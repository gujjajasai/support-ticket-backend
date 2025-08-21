package com.yourcompany.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Servlet Filter to handle Cross-Origin Resource Sharing (CORS) requests.
 * This filter is essential for allowing a separate frontend application (like a React app
 * running on localhost:3000) to communicate with this backend server.
 * It intercepts all incoming requests to add the necessary CORS headers to the response.
 */
@WebFilter("/*") // The "/*" pattern ensures this filter is applied to every request.
public class CorsFilter implements Filter {

    /**
     * This method is called by the container when the filter is first initialized.
     * We don't have any specific initialization logic, so it's left empty.
     *
     * @param fConfig A FilterConfig object containing the filter's configuration and initialization parameters.
     * @throws ServletException if an error occurs during initialization.
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // No initialization needed
    }

    /**
     * The core logic of the filter. This method is called for each incoming request.
     * It adds CORS headers and handles preflight OPTIONS requests.
     *
     * @param request  The ServletRequest object that contains the client's request.
     * @param response The ServletResponse object that contains the filter's response.
     * @param chain    The FilterChain for invoking the next filter or the target resource.
     * @throws IOException      if an I/O error occurs during processing.
     * @throws ServletException if a servlet error occurs during processing.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Cast the generic ServletResponse and ServletRequest to their HTTP-specific versions.
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // --- Set CORS Headers ---

        // Access-Control-Allow-Origin: This is the most important header.
        // It specifies which origin (domain) is allowed to access the resources on this server.
        // We set it to our React frontend's development server address.
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        // Access-Control-Allow-Methods: This header specifies the HTTP methods
        // (e.g., GET, POST) that are allowed from the foreign origin.
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        // Access-Control-Allow-Headers: This header indicates which HTTP headers
        // can be used during the actual request. "Content-Type" is essential for
        // sending JSON data from the frontend.
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type");

        // --- Handle Preflight Requests ---
        // A preflight request is an automatic "OPTIONS" request that the browser sends
        // before complex requests (like POST with a JSON body) to check if the server
        // understands and allows the actual request.
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            // If it's a preflight request, we just need to send back the headers we've set
            // with a 200 OK status. We don't need to proceed to the actual servlet.
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            // For all other requests (GET, POST, etc.), we call chain.doFilter().
            // This passes the request and response objects along to the next filter in the chain,
            // or to the target servlet if this is the last filter.
            chain.doFilter(request, response);
        }
    }

    /**
     * This method is called by the container when the filter is taken out of service.
     * We don't have any cleanup logic, so it's left empty.
     */
    @Override
    public void destroy() {
        // No cleanup needed
    }
}