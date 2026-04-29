# Aging Stock Identification
- Any stock (vehicle) that in inventory > 90 days will be marked as aging stock.
- "aging" definition: current date - inventoryReceiptDate.

## Requirement
- Add an attribute into VehicleResponse to mark if the vehicle is aging stock or not.
- When filtering by aging stock, return only aging stock.
- Filter should support filtering by a range of days in inventory. 

## Rules
- Write unit tests before implementation, let me review the tests first then continue to implementation
- Run all tests after implementation, only continue to the next step when all tests pass
- Only log neccessary information. Don't make the log to be polluted.