# API Design for Autonomous Customer Support System

This document outlines the REST API contract for the core Java backend. The API is designed to be secure, scalable, and serve as the contract between the React frontend, the Java backend, and the Node.js AI agent.

## 1. Authentication Endpoints
Handles user registration and login. A successful login returns a JSON Web Token (JWT) for securing subsequent requests.

| HTTP Method | URL Path           | Description                                                        | Request Body                                                               | Response Body                                                                              |
|-------------|--------------------|--------------------------------------------------------------------|----------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| `POST`      | `/api/auth/register` | Creates a new user account ('Customer' or 'Agent').                  | `{ "fullName": "...", "email": "...", "password": "...", "role": "Customer" }` | `{ "userId": 1, "email": "...", "message": "User registered successfully" }`              |
| `POST`      | `/api/auth/login`    | Authenticates a user and returns a JWT.                            | `{ "email": "...", "password": "..." }`                                       | `{ "token": "jwt.string", "user": { "userId": 1, "fullName": "...", "role": "Agent" } }` |
| `GET`       | `/api/auth/me`       | Retrieves the profile of the currently authenticated user (requires JWT). | (None)                                                                     | `{ "userId": 1, "fullName": "...", "email": "...", "role": "Customer" }`                 |

## 2. Ticket Management Endpoints
For creating, viewing, and managing tickets. Access is role-based to ensure customers only see their own tickets while agents have broader access.

| HTTP Method | URL Path                            | Description                                                                              | Request Body                                       | Response Body                                                                                              |
|-------------|-------------------------------------|------------------------------------------------------------------------------------------|----------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| `POST`      | `/api/tickets`                      | (Customer) Creates a new support ticket.                                                 | `{ "title": "...", "description": "...", "priority": "Medium" }` | `{ "ticketId": 101, "title": "...", "status": "Open", ... }`                                                  |
| `GET`       | `/api/tickets`                      | (Customer) Retrieves a list of tickets created by the authenticated customer.            | (None)                                             | `[ { "ticketId": 101, "title": "...", "status": "Open" }, ... ]`                                               |
| `GET`       | `/api/tickets/{ticketId}`           | (Customer/Agent) Retrieves full details of a specific ticket.                            | (None)                                             | `{ "ticketId": 101, "title": "...", "description": "...", "replies": [...] }`                               |
| `GET`       | `/api/agent/tickets`                | (Agent) Retrieves all tickets. Can be filtered by status (e.g., `?status=Open`).         | (None)                                             | `[ { "ticketId": 101, "customerName": "...", "title": "...", "status": "Open" }, ... ]`                     |
| `PUT`       | `/api/agent/tickets/{ticketId}/assign`| (Agent) Assigns a ticket to the authenticated agent.                                     | (None)                                             | `{ "message": "Ticket 101 assigned successfully." }`                                                       |
| `PUT`       | `/api/agent/tickets/{ticketId}/status`| (Agent) Updates the status or priority of a ticket.                                      | `{ "status": "In Progress", "priority": "High" }`    | `{ "ticketId": 101, "status": "In Progress", ... }`                                                          |

## 3. Reply Management Endpoints
Manages the conversation within a ticket.

| HTTP Method | URL Path                      | Description                                                  | Request Body           | Response Body                                                                      |
|-------------|-------------------------------|--------------------------------------------------------------|------------------------|------------------------------------------------------------------------------------|
| `GET`       | `/api/tickets/{ticketId}/replies` | (Customer/Agent) Retrieves all replies for a specific ticket. | (None)                 | `[ { "replyId": 501, "authorName": "...", "message": "...", "createdAt": "..." }, ... ]` |
| `POST`      | `/api/tickets/{ticketId}/replies` | (Customer/Agent) Adds a new reply to a ticket.               | `{ "message": "..." }`   | `{ "replyId": 502, "authorName": "...", "message": "...", "createdAt": "..." }`      |

## 4. Endpoints for the Autonomous Node.js AI Agent
These specialized endpoints are designed for the AI Agent to take direct, autonomous action on tickets based on its analysis.

| HTTP Method | URL Path                      | Description                                                  | Request Body                                                     | Response Body                                        |
|-------------|-------------------------------|--------------------------------------------------------------|------------------------------------------------------------------|------------------------------------------------------|
| `GET`       | `/api/ai/ticket-queue`        | (AI Agent) Fetches a ticket that requires AI processing.       | (None)                                                           | `{ "ticketId": 105, "title": "...", "description": "..." }` |
| `PUT`       | `/api/ai/tickets/{ticketId}`    | (AI Agent) Updates ticket status and tags after analysis (e.g., to escalate to a human). | `{ "status": "Needs Human Attention", "tags": ["Sensitive Data"] }` | `{ "message": "Ticket updated by AI Agent" }`        |
| `POST`      | `/api/ai/tickets/{ticketId}/reply` | (AI Agent) Posts an automated, AI-generated reply to a ticket. | `{ "message": "..." }`                                           | `{ "replyId": 102, "message": "Reply posted by AI" }` |