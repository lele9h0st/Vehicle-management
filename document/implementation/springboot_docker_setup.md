# 🐳 Docker Setup for Spring Boot + PostgreSQL

## 📌 Overview
This guide shows how to:
- Run a Spring Boot backend in Docker
- Use PostgreSQL as the database
- Connect both services using Docker Compose

---

## 📁 Project Structure

```
project-root/
│── backend/
│   ├── Dockerfile
│   ├── target/
│   │   └── app.jar
│   └── src/
│
│── docker-compose.yml
│── .env
```

---

## ⚙️ 1. Spring Boot Configuration

Update `application.yml` or `application.properties`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/mydb
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

> ⚠️ Use `postgres` as the hostname (matches the service name in Docker Compose)

---

## 🐳 2. Backend Dockerfile

Create `backend/Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/app.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🐘 3. Docker Compose Setup

Create `docker-compose.yml` in root:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: postgres_db
    restart: always
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  backend:
    build: ./backend
    container_name: springboot_app
    depends_on:
      - postgres
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/mydb
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres

volumes:
  postgres_data:
```

---

## 🔐 4. Environment Variables (Optional)

Create `.env` file:

```
POSTGRES_DB=mydb
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
```

Update `docker-compose.yml` to use:

```yaml
env_file:
  - .env
```

---

## 🚀 5. Build & Run

### Step 1: Build Spring Boot JAR

```bash
./mvnw clean package
```

or

```bash
mvn clean package
```

---

### Step 2: Run Docker Compose

```bash
docker-compose up --build
```

---

### Step 3: Access Services

- Backend API: http://localhost:8080
- PostgreSQL: localhost:5432

---

## 🧪 6. Verify Connection

Check logs:

```bash
docker logs springboot_app
```

Look for:
```
Started Application
```

---

## 🛠️ 7. Common Issues

### ❌ Cannot connect to database
- Ensure hostname is `postgres`, NOT `localhost`

### ❌ Port already in use
- Change ports in `docker-compose.yml`

### ❌ App starts before DB ready
Add retry logic or use:

```yaml
depends_on:
  - postgres
```

(For production, use health checks)

---

## 🔄 8. Rebuild Clean

```bash
docker-compose down -v
docker-compose up --build
```

---

## ✅ Done!

You now have:
- Spring Boot running in Docker
- PostgreSQL running in Docker
- Both connected via Docker network
