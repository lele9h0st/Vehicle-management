# Analyzation and Planing
I will choose Scenario B for this assessment.

## Assumption:
- "age" definition: `currentDate - dateAddedToInventory`.
- Dashboard will be used by dealership managers in real-time.
- "real-time" definition: 
  - new added vehicle/ a vehicle sold: update status quickly.
  - Price/status changes need to updated quickly
  - Solution: Polling interval + WebSocket
- Type of statuses/actions: Can add or remove types
- Manager has already authenticated and authorised to access the APIs. Id of the manager will be there name 

**Diagrams**
- Database Schema: https://drive.google.com/file/d/1LW2kAVR0uuHBpARYapmPWq8EUByTrfdN/view?usp=sharing

**Key components**:
- **Inventory Service**: Fetches and returns vehicle data
- **Filter Service**: Applies dynamic filtering (make, model, age range, etc.)
- **Aging Calculator**: Computes `daysInInventory` and flags vehicles >90 days
- **Insight Service**: Handles CRUD for manager actions/statuses on aging vehicles

**Technologies**
- real-time data refresh: interval polling + WebSocket. 
  - Interval polling: API endpoint to fetch all vehicles data with filters. API consumer can choose interval time to fetch data (5s, 15s, 1m, 5m, etc).
  - Websocket: for specific item information status ()
- Database: PostgreSQL
- Backend framework: Springboot

**Observability**
- Logging: 
  - Log4j2 
- Metrics
  - Request count
  - Error rate
  - Response time


**Considering**
- Hibernate enver for history records