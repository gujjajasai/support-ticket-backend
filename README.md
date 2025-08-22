# Autonomous AI Customer Support System - Core Backend (Java)

This is the core backend service for a full-stack, AI-powered customer support application. Built with foundational Java web technologies (Servlets, JDBC) and professional-grade practices like connection pooling, this service is responsible for all primary business logic, data persistence, and user authentication.

---

## About The Project

This service acts as the central "source of truth" and orchestrator for the entire application. It provides a secure REST API for the frontend to consume and manages calls to other specialized microservices (like the AI Agent).

This project demonstrates skills in building robust, reliable, and scalable backend systems using industry-standard Java patterns.

### Key Features:
*   **RESTful API:** Provides secure endpoints for user registration, login, and CRUD (Create, Read, Update, Delete) operations for tickets.
*   **Role-Based Logic:** Implements access control, ensuring customers and agents can only access the data they are permitted to see.
*   **Professional Database Management:** Uses a **HikariCP connection pool** for highly efficient and resilient communication with a cloud PostgreSQL database.
*   **Microservice Orchestration:** Acts as the controller that calls the external Node.js AI Agent for analysis, demonstrating a distributed architecture.
*   **Servlet-Based:** Built on the foundational Java Servlet API, showcasing a deep understanding of core web principles.

### Tech Stack:
*   **Language:** Java
*   **Web Technologies:** Servlets, JSP
*   **Database:** PostgreSQL (with JDBC)
*   **Connection Pooling:** HikariCP
*   **Build Tool:** Maven
*   **JSON Parsing:** Gson
*   **Deployment:** Render (via Docker)

---

## Architecture

This backend is the central hub in a three-service application:

1.  **[Frontend (Next.js)](https://github.com/gujjajasai/support-ticket-frontend):** The user interface that consumes this backend's API. 
2.  **Core Backend (This Repository):** The main application server.
3.  **[AI Agent (Node.js)](https://github.com/gujjajasai/ai-agent-service):** A specialized microservice called by this backend for AI tasks. 

---

## Getting Started

To run this project locally:

1.  Ensure you have Java (JDK 11+) and Maven installed.
2.  Set up a PostgreSQL database (e.g., using Neon) and update the credentials in `src/main/java/com/yourcompany/util/DatabaseConnectionManager.java`.
3.  Run the application using the Tomcat Maven plugin:
    ```sh
    mvn tomcat7:run
    ```
    The server will start on `http://localhost:8088`.