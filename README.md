# Project Name

## Description

A system to create invoices, make payments, and manage overdue invoices. It provides APIs to handle these tasks using Spring Boot.

## Setup

1. **Clone the repository:**

    ```bash
    git clone https://github.com/deekshitha16/invoice-tracker-task.git
    ```

2. **Navigate to the project directory:**

    ```bash
    cd invoice-tracker-task
    ```

3. **Build and run the Docker Compose services:**

    ```bash
    docker-compose up --build
    ```

   This will start the application and map it to port `8080` on your localhost.

## API

### Endpoints

#### 1. **Create Invoice**

- **Endpoint:** `POST /invoices`
- **cURL Request:**
    ```bash
    curl -X POST http://localhost:8080/invoices \
         -H "Content-Type: application/json" \
         -d '{"amount": 100.00, "dueDate": "2024-12-31"}'
    ```

#### 2. **Get All Invoices**

- **Endpoint:** `GET /invoices`
- **cURL Request:**
    ```bash
    curl -X GET http://localhost:8080/invoices
    ```

#### 3. **Pay Invoice**

- **Endpoint:** `POST /invoices/{invoiceId}/payments`
- **cURL Request:**
    ```bash
    curl -X POST http://localhost:8080/invoices/1/payments \
         -H "Content-Type: application/json" \
         -d '{"amount": 100.00}'
    ```

#### 4. **Process Overdue Payments**

- **Endpoint:** `POST /invoices/process-overdue`
- **cURL Request:**
    ```bash
    curl -X POST http://localhost:8080/invoices/process-overdue \
         -H "Content-Type: application/json" \
         -d '{"overdueDays": 30, "lateFee": 10.00}'
    ```

## Assumptions

- The project requires Docker and assumes the server is running at `http://localhost:8080`.
- **Database:** The project uses an H2 in-memory database for development and testing purposes. The H2 database is configured to run in-memory and will not persist data beyond the application's lifecycle.
- **DTO Validation for `InvoiceCreateRequestDTO`:**
    - `amount` cannot be null and must be greater than 0.
    - `dueDate` cannot be null and must be today or a future date.
- **DTO Validation for `InvoicePaymentRequestDTO`:**
    - `amount` cannot be null and must be greater than 0.
- **DTO Validation for `InvoiceOverduePaymentRequestDTO`:**
    - `lateFee` cannot be null and should not be negative.
    - `overdueDays` cannot be null and must be at least 1.

## Additional Functionality
- Added soft Delete functionality for the Invoice.
- Query only the active Invoices.
