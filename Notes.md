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

The API can be tested using Postman or curl. All endpoints require the `X-API-KEY` header (default: `changeme-local-dev-key`). `GET /api/v1/employee` returns all employees, `GET /api/v1/employee/{uuid}` returns a specific employee or `404` if it doesn't exist, and `POST /api/v1/employee` creates a new employee and returns `201 Created`. Invalid input returns `400 Bad Request`, while requests without a valid API key return `401 Unauthorized`.