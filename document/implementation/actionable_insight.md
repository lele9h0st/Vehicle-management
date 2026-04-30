# Actionable Insights:
Allow a manager to log and persist a status or proposed action for each aging vehicle 

# Requirement:
- POST endpoint for manager to persist action for aging vehicle.
- PUT endpoint for manager to edit action for an aging vehicle.
- DELETE endpoint for manager to remove action for an aging vehicle.
- GET endpoint for manager to fetch actions of an aging vehicle
- All http action need to verify if the vehicle the manager request to read/persist/modify is actual a aging vehicle
  - If not the return 422 with proper messages.
- Handle multi managers modify an action concurrently.


# Rules:
- write unit tests before implementation, let me review the tests first then continue to implementation
- run all tests after implementation, only continue to the next step when all tests pass
- Only log neccessary information. Don't make the log to be polluted.