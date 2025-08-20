-- Active: 1736442537724@@127.0.0.1@5432@avion@public
-- Table roles
DROP TABLE IF EXISTS roles CASCADE;
CREATE TABLE roles(
   id SMALLSERIAL,
   label VARCHAR(50) NOT NULL,
   PRIMARY KEY(id)
);

-- Table users
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users(
   id SERIAL PRIMARY KEY,
   email VARCHAR(150) NOT NULL UNIQUE,
   username VARCHAR(100) NOT NULL,
   pwd VARCHAR(255) NOT NULL
);

-- Table user_roles
DROP TABLE IF EXISTS user_roles CASCADE;
CREATE TABLE user_roles(
   user_id INTEGER NOT NULL,
   role_id INTEGER NOT NULL,
   PRIMARY KEY(user_id, role_id),
   FOREIGN KEY(user_id) REFERENCES users(id),
   FOREIGN KEY(role_id) REFERENCES roles(id)
);

-- Trigger: Automatically add the 'USER' role for each new user
CREATE OR REPLACE FUNCTION add_user_role()
RETURNS TRIGGER AS $$
DECLARE
   user_role_id INTEGER;
BEGIN
   -- Retrieve the ID of the 'USER' role
   SELECT id INTO user_role_id FROM roles WHERE label = 'USER' LIMIT 1;

   -- Insert the new user with the 'USER' role into user_roles
   INSERT INTO user_roles (user_id, role_id) VALUES (NEW.id, user_role_id);

   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger that fires after each insert on users
DROP TRIGGER IF EXISTS trigger_add_user_role ON users;
CREATE TRIGGER trigger_add_user_role
AFTER INSERT ON users
FOR EACH ROW
EXECUTE FUNCTION add_user_role();

-- Enum type for class type  
DROP TYPE IF EXISTS class_type;
CREATE TYPE class_type AS ENUM ('ECONOMY', 'BUSINESS');
DROP TABLE IF EXISTS class CASCADE;
CREATE TABLE class (
    id SERIAL PRIMARY KEY,
    label class_type NOT NULL
);

-- Table airplanes
DROP TABLE IF EXISTS airplanes CASCADE;
CREATE TABLE airplanes (
   id SERIAL PRIMARY KEY,
   model VARCHAR(100) NOT NULL,
   total_seats INTEGER NOT NULL
);

DROP TABLE IF EXISTS airplane_class CASCADE;
CREATE TABLE airplane_class (
    airplane_id INTEGER NOT NULL REFERENCES airplanes(id) ON DELETE CASCADE,
    class_id INTEGER NOT NULL REFERENCES class(id) ON DELETE CASCADE,
    seat_count INTEGER NOT NULL,
    PRIMARY KEY (airplane_id, class_id)
);

SELECT seat_count FROM airplane_class WHERE airplane_id = 1 AND class_id = 1;

-- Table country
DROP TABLE IF EXISTS country CASCADE;
CREATE TABLE country (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Table cities
DROP TABLE IF EXISTS cities CASCADE;
CREATE TABLE cities (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country_id INTEGER REFERENCES country(id) ON DELETE SET NULL
);

-- Table flights
DROP TABLE IF EXISTS flights CASCADE;
CREATE TABLE flights (
    id SERIAL PRIMARY KEY,
    flight_number VARCHAR(10) UNIQUE NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    reservation_cutoff_hours INTEGER NOT NULL,  -- cutoff time in hours before departure to reserve
    cancellation_cutoff_hours INTEGER NOT NULL, -- cutoff time in hours before departure to cancel
    airplane_id INTEGER REFERENCES airplanes(id) NOT NULL,
    departure_city_id INTEGER REFERENCES cities(id) NOT NULL,
    arrival_city_id INTEGER REFERENCES cities(id) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table passenger_type 
DROP TABLE IF EXISTS passenger_type CASCADE;
CREATE TABLE passenger_type (
    id SERIAL PRIMARY KEY,
    type_name VARCHAR(10) NOT NULL,
    start_age SMALLINT,
    end_age SMALLINT
);

-- Table flight_class_passenger
DROP TABLE IF EXISTS flight_class_passenger CASCADE;
CREATE TABLE flight_class_passenger (
    id SERIAL PRIMARY KEY,
    flight_id INTEGER NOT NULL REFERENCES flights(id) ON DELETE CASCADE,
    class_id INTEGER NOT NULL REFERENCES class(id) ON DELETE CASCADE,
    passenger_type_id INTEGER NOT NULL REFERENCES passenger_type(id) ON DELETE CASCADE,
    promotion_limit INTEGER NOT NULL,          -- number of reservations eligible for the promotion for this flight/class
    promotion_discount NUMERIC(5,2) NOT NULL DEFAULT 0.00, -- pourcentage of discount
    base_price NUMERIC(10, 2) NOT NULL, 
    UNIQUE(flight_id, class_id, passenger_type_id)
);

-- Table config_fares
DROP TABLE IF EXISTS config_fares CASCADE;
CREATE TABLE config_fares (
    id SERIAL PRIMARY KEY,
    passenger_type_id INTEGER NOT NULL REFERENCES passenger_type(id) ON DELETE CASCADE,
    price NUMERIC(10,2) NOT NULL,
    UNIQUE(passenger_type_id)
);
ALTER TABLE config_fares
ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Table reservations
DROP TABLE IF EXISTS reservations CASCADE;
CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    flight_id INTEGER NOT NULL REFERENCES flights(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'RESERVED' CHECK (status IN ('RESERVED', 'CANCELLED', 'PAID', 'PENDING')),
    total_amount NUMERIC(10,2),
    total_discount NUMERIC(5,2) DEFAULT 0.00,
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reservation_passengers (
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    passenger_name VARCHAR(100) NOT NULL,
    passenger_birthdate DATE NOT NULL,
    passenger_type_id INTEGER REFERENCES passenger_type(id),
    class_id INTEGER NOT NULL REFERENCES class(id) ON DELETE CASCADE,
    base_price NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    discount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    final_price NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    promo_applied BOOLEAN NOT NULL DEFAULT FALSE,
    file_path_passport VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

---------------------------------------------------------------------
-- Table configurations  
---------------------------------------------------------------------
DROP TYPE IF EXISTS key_type CASCADE;
CREATE TYPE key_type AS ENUM(
    'reservation_cutoff_hours',
    'cancellation_cutoff_hours',
    'promotion_limit',
    'promotion_discount'
);
DROP TABLE IF EXISTS configurations CASCADE;
CREATE TABLE configurations (
    config_key key_type PRIMARY KEY,
    config_value VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

-- Alea
CREATE TABLE promotion(
    id INTEGER PRIMARY KEY,
    flight_id INTEGER NOT NULL REFERENCES flights(id),
    class_id INTEGER NOT NULL REFERENCES class(id),
    promotion_limit INTEGER NOT NULL,          -- number of reservations eligible for the promotion for this flight
    promotion_discount NUMERIC(10,2) NOT NULL DEFAULT 0.00, --
    base_price NUMERIC(10, 2) NOT NULL,
    deadline TIMESTAMP NOT NULL -- deadline for the promotion
);
INSERT INTO promotion (id, flight_id, class_id, promotion_limit, promotion_discount, base_price, deadline)
VALUES (1, 5, 1, 2, 300.00, 300.00, '2025-09-03 23:59:59');
INSERT INTO promotion (id, flight_id, class_id, promotion_limit, promotion_discount, base_price, deadline)
VALUES (2, 5, 1, 4, 200.00, 300.00, '2025-09-27 23:59:59');


INSERT INTO promotion (id, flight_id, class_id, promotion_limit, promotion_discount, base_price, deadline)
VALUES (3, 6, 1, 3, 350.00, 300.00, '2025-09-05 23:59:59');
INSERT INTO promotion (id, flight_id, class_id, promotion_limit, promotion_discount, base_price, deadline)
VALUES (4, 6, 1, 1, 400.00, 300.00, '2025-09-13 23:59:59');



-- CA par promotion
WITH paid AS (
  SELECT
      p.id                    AS promotion_id,
      p.flight_id,
      p.class_id,
      p.promotion_limit,
      p.promotion_discount,
      COUNT(rp.id)            AS paid_passengers
  FROM promotion p
  JOIN reservations r
        ON r.flight_id = p.flight_id
  JOIN reservation_passengers rp
        ON rp.reservation_id = r.id
       AND rp.class_id       = p.class_id
  WHERE r.status = 'PAID'
    AND r.created_at <= p.deadline
  GROUP BY p.id, p.flight_id, p.class_id, p.promotion_limit, p.promotion_discount
)
SELECT
  promotion_id,
  flight_id,
  class_id,
  paid_passengers,
  LEAST(paid_passengers, promotion_limit)                       AS qty_billed,
  (LEAST(paid_passengers, promotion_limit) * promotion_discount) AS revenue
FROM paid
ORDER BY flight_id, class_id, promotion_id;

-- CA global
WITH paid AS (
  SELECT
      p.id                    AS promotion_id,
      p.promotion_limit,
      p.promotion_discount,
      COUNT(rp.id)            AS paid_passengers
  FROM promotion p
  JOIN reservations r
        ON r.flight_id = p.flight_id
  JOIN reservation_passengers rp
        ON rp.reservation_id = r.id
       AND rp.class_id       = p.class_id
  WHERE r.status = 'PAID'
    AND r.created_at <= p.deadline
  GROUP BY p.id, p.promotion_limit, p.promotion_discount
)
SELECT
  SUM(LEAST(paid_passengers, promotion_limit) * promotion_discount)::numeric(16,2)
    AS total_revenue
FROM paid;



---------------------------------------------------------------------
-- Trigger and function to enforce business rules:
-- 1. Verify that the reservation is made sufficiently early (before departure_time minus reservation_cutoff_hours).
-- 2. For a new reservation, assign the promotion to the first eligible reservations based on the limit.
-- 3. When cancelling (updating status to 'cancelled'), verify that the cancellation occurs before departure_time minus cancellation_cutoff_hours.
-- 4. Verify that the selected flight class corresponds to the flight.
---------------------------------------------------------------------