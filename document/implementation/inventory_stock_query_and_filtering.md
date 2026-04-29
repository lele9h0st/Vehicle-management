## Requirement
- Service to fetch vehicle from database with make, model, year, status, price, mileage, inventory_receipt_date, created_at, updated_by, updated_at
- The service provide function to query with filterable request vehicle by make, model, year, status, price, mileage, inventory_receipt_date, created_at, updated_by, updated_at
- The filtering should be applied at database level.
- Pagination should apply for any request of filtering.
- Request payload should provide filter attributes, page attributes
- Response payload should provide list of vehicle, pagination information
- Sanitize and handle gracefully any invalid input. Return appropriate error response. If validation error happen, log it and return error response
- Endpoint url /api/v1/inventory/stock 

## Non functional requirements
- response time shoud be less than 100ms for 95% of requests
- ratelimiter should be applied for any request, only 20 requests per 10 seconds

# Rules:
- write unit tests before implementation, let me review the tests first then continue to implementation
- run all tests after implementation, only continue to the next step when all tests pass
- Only log neccessary information. Don't make the log to be polluted.