# Database Schema

## SQL Schema Definition

```sql
-- Model Table
CREATE TABLE Model (
    id INT PRIMARY KEY AUTO_INCREMENT,
    make VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    year INT NOT NULL
);

-- Vehicle Table
CREATE TABLE Vehicle (
    id INT PRIMARY KEY AUTO_INCREMENT,
    model_id INT NOT NULL,
    VIN VARCHAR(17) UNIQUE NOT NULL,
    status VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    mileage INT NOT NULL,
    inventory_receipt_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (model_id) REFERENCES Model(id)
);

-- Action Table
CREATE TABLE Action (
    id INT PRIMARY KEY AUTO_INCREMENT,
    vehicle_id INT NOT NULL,
    status VARCHAR(50),
    type VARCHAR(50),
    note TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(id)
);
```

## Schema Relationships

- **Model** → **Vehicle**: One-to-many relationship (Model.id → Vehicle.model_id)
- **Vehicle** → **Action**: One-to-many relationship (Vehicle.id → Action.vehicle_id)

## Table Descriptions

### Model
Stores vehicle model information including make, name, and year.

### Vehicle
Stores individual vehicle records with details like VIN, status, price, mileage, and inventory dates. Each vehicle belongs to a model.

### Action
Stores actions or events associated with vehicles, including status changes and notes. Each action is linked to a specific vehicle.
