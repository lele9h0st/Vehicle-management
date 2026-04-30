# improve /stock endpoint - caching

# requirement
- Add redis to cache the request from GET /stock. 
- Key will be hash of request
- Value will be list of vehicles.
- Set TTL is 5 mins + offset, offset range from -10s to +10s
- Any vehicle update/add in database will invalidation all caches
- Log if cache hit or miss

## Local Manual test
- Rename the PingController into TestController
- create PUT API to update a Vehicle, input is Vehicle Id and status. This PUT API will trigger the invalidation mechanism mentioned in requirement section
- Create an API to get all data tha are caching

## Rules
- Write unit tests before implementation, let me review the tests first then continue to implementation
- Run all tests after implementation, only continue to the next step when all tests pass
- Only log neccessary information. Don't make the log to be polluted.