# Notes

## Structure
- controller - handles HTTP requests/responses
- service - business logic (interface + implementation)
- model - Employee interface (given) and MockEmployee, a fake in-memory implementation
- dto - what actually goes in/out of the API (CreateEmployeeRequest, EmployeeResponse)
- exception - custom exception + error response shape
- security - API key check

## Why DTOs
Employee is an interface, so it can't be used directly for the request body - Jackson can't build an object
from an interface. Also didn't want the client setting things like uuid or hire date, those should be set by
the server. Same reasoning for the response side - keeps what the client sees separate from the internal model,
so if the internal model changes later it doesn't break the API.

## Exception handling
The service just throws a normal exception (EmployeeNotFoundException) when it can't find someone. The
controller catches it and decides the status code (404). Same for bad input (400) and anything unexpected
(500), so nothing sends back a raw stack trace.

## Security
All endpoints need an API key in the X-API-KEY header. Since this API gets called by another system
(Employees-R-US), not a browser, a shared key made more sense than login/session based auth.

## Assumptions
- Spring Boot 3 (jakarta imports, not javax)
- No real database - in-memory list of fake employees created on startup, since the assignment says not to
  worry about persistence

## Running it
```
./gradlew.bat bootRun
```
```
curl http://localhost:8080/api/v1/employee -H "X-API-KEY: <key>"
```

## Testing

The API can be tested using Postman or `curl`. All requests require the `X-API-KEY` header.

**Base URL**

```
http://localhost:8080/api/v1/employee
```

**Default API Key**

```
changeme-local-dev-key
```

### 1. Get All Employees

Request

```bash
curl -X GET http://localhost:8080/api/v1/employee \
  -H "X-API-KEY: changeme-local-dev-key"
```

Expected Result

- Status: `200 OK`
- Returns a JSON array containing all employees.

---

### 2. Get Employee by UUID

Replace `<employee-uuid>` with a valid UUID returned from the previous request.

```bash
curl -X GET http://localhost:8080/api/v1/employee/<employee-uuid> \
  -H "X-API-KEY: changeme-local-dev-key"
```
for eg: UUID = dceb7cc2-e423-4fed-bc54-7e5936e9879a
Expected Result

- Status: `200 OK`
- Returns the requested employee.

If the UUID does not exist:

- Status: `404 Not Found`
- Returns an error response.

---

### 3. Create Employee

```bash
curl -X POST http://localhost:8080/api/v1/employee \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: changeme-local-dev-key" \
  -d '{
        "firstName":"John",
        "lastName":"Doe",
        "email":"john.doe@example.com",
        "phoneNumber":"9876543210"
      }'
```

Expected Result

- Status: `201 Created`
- Returns the newly created employee including its generated UUID.

---

### 4. Invalid Request

Send a request with missing or invalid fields.

Expected Result

- Status: `400 Bad Request`
- Returns validation error details.

---

### 5. Invalid or Missing API Key

```bash
curl -X GET http://localhost:8080/api/v1/employee
```

Expected Result

- Status: `401 Unauthorized`

---

### 6. Internal Server Error

Any unexpected exception is handled by the global exception handler.

Expected Result

- Status: `500 Internal Server Error`
- Returns a standardized error response without exposing internal implementation details.