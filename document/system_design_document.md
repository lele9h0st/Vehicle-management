# Analyzation and Planing
I will choose Scenario B for this assessment.

## Assumption:
- "age" definition: `currentDate - dateAddedToInventory`.
- Dashboard will be used by dealership managers in real-time.
- "real-time" definition: 
  - new added vehicle/ a vehicle sold: update status quickly.
  - Price/status changes need to updated quickly
  - Solution: Polling interval
- Type of statuses/actions: Can add or remove types
- Manager has already authenticated and authorised to access the APIs. Id of the manager will be there name 

**Diagrams**
- Database Schema: https://drive.google.com/file/d/1LW2kAVR0uuHBpARYapmPWq8EUByTrfdN/view?usp=sharing
- Sequence Diagram:
  - Inventory Visualization: https://drive.google.com/file/d/1K8I1g89qSSvFl1oOBm_wR6bi-sqXg5gL/view?usp=sharing
  - Log Action for Aging Vehicle: https://drive.google.com/file/d/1AMxPopkJmAjSgdd3XaAGy6pkFNtaDi7Z/view?usp=sharing



**Key components**:
- **API Layer (controller)**: Handles REST requests for Inventory (searching/filtering) and Actions (managing aging stock tasks). Includes a GlobalExceptionHandler for consistent error responses.
- **Business Logic (service)**: Enforces domain rules, such as the 90-day aging requirement for vehicle actions and dynamic search filtering using JPA Specifications.
- **Data Models (domain)**: Core JPA entities (Vehicle, Action) representing the system's state and business objects.
- **Persistence (repository)**: Spring Data JPA interfaces for database operations, supported by Flyway for schema migrations and PostgreSQL for storage.
- **Infrastructure (config)**:
  - Redis: High-speed caching for inventory queries.
  - Rate Limiting: Protects API via Bucket4j (e.g., 20 requests/10s).
  - Logging Cache: Monitors cache hits/misses for performance.
- **DevOps**: Docker Compose orchestrates the backend, database, and cache for a unified local environment.

**Technologies**
- real-time data refresh: interval polling. 
  - Interval polling: API endpoint to fetch all vehicles data with filters. API consumer can choose interval time to fetch data (5s, 15s, 1m, 5m, etc).
- Database: PostgreSQL
- Backend framework: Springboot

**Observability**
- Logging: 
  - Log4j2 


**Considering**
- Hibernate enver for history records

**How AI Agent assist me in design phrase**
- At first I convert the assessment from pdf into md file so that AI can easily understand and follow all the requirements.
- Then I asked AI to generate me an insight report for the given assessment to get a high-level understanding of the business logic and requirements.
- Then I choose the scenario B and ask AI generate an architecture document based on that scenario (scenario_b_architecture.html).
- With the architecture document from AI, I finetune it and make final design decision.