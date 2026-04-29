-- R__Insert_Mock_Vehicles.sql
-- Repeatable migration to insert mock data for testing purposes

-- 1. Insert Makes
INSERT INTO make (name) VALUES ('Toyota') ON CONFLICT (name) DO NOTHING;
INSERT INTO make (name) VALUES ('Honda') ON CONFLICT (name) DO NOTHING;
INSERT INTO make (name) VALUES ('Ford') ON CONFLICT (name) DO NOTHING;
INSERT INTO make (name) VALUES ('Tesla') ON CONFLICT (name) DO NOTHING;
INSERT INTO make (name) VALUES ('BMW') ON CONFLICT (name) DO NOTHING;

-- 2. Insert Models
INSERT INTO model (make_id, name)
SELECT id, 'Camry' FROM make WHERE name = 'Toyota'
AND NOT EXISTS (SELECT 1 FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Toyota' AND m.name = 'Camry');

INSERT INTO model (make_id, name)
SELECT id, 'Civic' FROM make WHERE name = 'Honda'
AND NOT EXISTS (SELECT 1 FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Honda' AND m.name = 'Civic');

INSERT INTO model (make_id, name)
SELECT id, 'F-150' FROM make WHERE name = 'Ford'
AND NOT EXISTS (SELECT 1 FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Ford' AND m.name = 'F-150');

INSERT INTO model (make_id, name)
SELECT id, 'Model 3' FROM make WHERE name = 'Tesla'
AND NOT EXISTS (SELECT 1 FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Tesla' AND m.name = 'Model 3');

INSERT INTO model (make_id, name)
SELECT id, '3 Series' FROM make WHERE name = 'BMW'
AND NOT EXISTS (SELECT 1 FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'BMW' AND m.name = '3 Series');

INSERT INTO model (make_id, name)
SELECT id, 'Corolla' FROM make WHERE name = 'Toyota'
AND NOT EXISTS (SELECT 1 FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Toyota' AND m.name = 'Corolla');

INSERT INTO model (make_id, name)
SELECT id, 'Accord' FROM make WHERE name = 'Honda'
AND NOT EXISTS (SELECT 1 FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Honda' AND m.name = 'Accord');

INSERT INTO model (make_id, name)
SELECT id, 'Mustang' FROM make WHERE name = 'Ford'
AND NOT EXISTS (SELECT 1 FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Ford' AND m.name = 'Mustang');

-- 3. Insert Vehicles
INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date, created_at, updated_by, updated_at)
SELECT m.id, 2023, 'VIN001', 'AVAILABLE', 35000, 100, CURRENT_DATE, CURRENT_TIMESTAMP - INTERVAL '1 day', 'admin', CURRENT_TIMESTAMP - INTERVAL '1 day'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Toyota' AND m.name = 'Camry'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date, created_at, updated_by, updated_at)
SELECT m.id, 2022, 'VIN002', 'SOLD', 25000, 5000, CURRENT_DATE - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'admin', CURRENT_TIMESTAMP - INTERVAL '2 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Honda' AND m.name = 'Civic'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2021, 'VIN003', 'AVAILABLE', 45000, 20000, CURRENT_DATE - INTERVAL '20 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Ford' AND m.name = 'F-150'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2023, 'VIN004', 'AVAILABLE', 50000, 500, CURRENT_DATE - INTERVAL '5 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Tesla' AND m.name = 'Model 3'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2020, 'VIN005', 'AVAILABLE', 30000, 40000, CURRENT_DATE - INTERVAL '30 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'BMW' AND m.name = '3 Series'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2022, 'VIN006', 'AVAILABLE', 32000, 15000, CURRENT_DATE - INTERVAL '15 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Toyota' AND m.name = 'Camry'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2021, 'VIN007', 'AVAILABLE', 22000, 30000, CURRENT_DATE - INTERVAL '25 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Honda' AND m.name = 'Civic'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2022, 'VIN008', 'SOLD', 48000, 10000, CURRENT_DATE - INTERVAL '12 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Ford' AND m.name = 'F-150'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2022, 'VIN009', 'AVAILABLE', 48000, 8000, CURRENT_DATE - INTERVAL '8 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Tesla' AND m.name = 'Model 3'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2021, 'VIN010', 'AVAILABLE', 35000, 25000, CURRENT_DATE - INTERVAL '22 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'BMW' AND m.name = '3 Series'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2021, 'VIN011', 'SOLD', 28000, 45000, CURRENT_DATE - INTERVAL '40 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Toyota' AND m.name = 'Camry'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2023, 'VIN012', 'AVAILABLE', 26000, 200, CURRENT_DATE - INTERVAL '2 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Honda' AND m.name = 'Civic'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2023, 'VIN013', 'AVAILABLE', 23000, 50, CURRENT_DATE
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Toyota' AND m.name = 'Corolla'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2022, 'VIN014', 'AVAILABLE', 28000, 3000, CURRENT_DATE - INTERVAL '7 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Honda' AND m.name = 'Accord'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2023, 'VIN015', 'AVAILABLE', 40000, 1000, CURRENT_DATE - INTERVAL '3 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Ford' AND m.name = 'Mustang'
ON CONFLICT (vin) DO NOTHING;

INSERT INTO vehicle (model_id, year, vin, status, price, mileage, inventory_receipt_date)
SELECT m.id, 2022, 'VIN016', 'SOLD', 21000, 12000, CURRENT_DATE - INTERVAL '15 days'
FROM model m JOIN make mk ON m.make_id = mk.id WHERE mk.name = 'Toyota' AND m.name = 'Corolla'
ON CONFLICT (vin) DO NOTHING;
