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
    class class_type NOT NULL
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

-- Table flight_class
DROP TABLE IF EXISTS flight_class CASCADE;
CREATE TABLE flight_class (
    id SERIAL PRIMARY KEY,
    flight_id INTEGER NOT NULL REFERENCES flights(id) ON DELETE CASCADE,
    class_id INTEGER NOT NULL REFERENCES class(id) ON DELETE CASCADE,
    promotion_limit INTEGER NOT NULL,          -- number of reservations eligible for the promotion for this flight/class
    promotion_discount NUMERIC(5,2) NOT NULL DEFAULT 0.00, -- pourcentage of discount
    UNIQUE(flight_id, class_id)
);

-- Table passenger_type 
DROP TABLE IF EXISTS passenger_type CASCADE;
CREATE TABLE passenger_type (
    id SERIAL PRIMARY KEY,
    type_name VARCHAR(10) NOT NULL
);

-- Table tariffs
DROP TABLE IF EXISTS fares CASCADE;
CREATE TABLE fares (
    id SERIAL PRIMARY KEY,
    flight_id INTEGER NOT NULL REFERENCES flights(id) ON DELETE CASCADE,
    passenger_type_id INTEGER NOT NULL REFERENCES passenger_type(id) ON DELETE CASCADE,
    base_price NUMERIC(10,2) NOT NULL,
    UNIQUE(flight_id, passenger_type_id)
);

-- Table config_fares
DROP TABLE IF EXISTS config_fares CASCADE;
CREATE TABLE config_fares (
    id SERIAL PRIMARY KEY,
    passenger_type_id INTEGER NOT NULL REFERENCES passenger_type(id) ON DELETE CASCADE,
    price NUMERIC(10,2) NOT NULL,
    UNIQUE(passenger_type_id)
);

-- Table reservations
DROP TABLE IF EXISTS reservations CASCADE;
CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    flight_id INTEGER NOT NULL REFERENCES flights(id) ON DELETE CASCADE,
    reservation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'RESERVED' CHECK (status IN ('RESERVED', 'CANCELLED', 'PAID', 'PENDING')),
    total_amount NUMERIC(10,2),
    cancellation_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table reservation_details
DROP TABLE IF EXISTS reservation_details CASCADE;
CREATE TABLE reservation_details (
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    passenger_type_id INTEGER NOT NULL REFERENCES passenger_type(id) ON DELETE CASCADE,
    passenger_name VARCHAR(100) NOT NULL,
    file_path_passport VARCHAR(255),
    price NUMERIC(10,2) NOT NULL,
    discount NUMERIC(5,2) DEFAULT 0.00,
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

---------------------------------------------------------------------
-- Trigger and function to enforce business rules:
-- 1. Verify that the reservation is made sufficiently early (before departure_time minus reservation_cutoff_hours).
-- 2. For a new reservation, assign the promotion to the first eligible reservations based on the limit.
-- 3. When cancelling (updating status to 'cancelled'), verify that the cancellation occurs before departure_time minus cancellation_cutoff_hours.
-- 4. Verify that the selected flight class corresponds to the flight.
---------------------------------------------------------------------

-- Function to enforce business rules
CREATE OR REPLACE FUNCTION enforce_reservation_rules()
RETURNS TRIGGER AS $$
DECLARE
    flight_record RECORD;
    cutoff_time TIMESTAMP;
    promotion_available BOOLEAN;
    promotion_count INTEGER;
BEGIN
    -- Retrieve flight details
    SELECT * INTO flight_record
    FROM flights
    WHERE id = NEW.flight_id;

    -- Rule 1: Verify reservation is made before the cutoff time
    cutoff_time := flight_record.departure_time - (flight_record.reservation_cutoff_hours * INTERVAL '1 hour');
    IF NEW.reservation_date > cutoff_time THEN
        RAISE EXCEPTION 'Reservation cannot be made after the cutoff time: %', cutoff_time;
    END IF;

    -- Rule 2: Assign promotion to the first eligible reservations
    IF NEW.status = 'RESERVED' THEN
        -- Check if promotion is available for the flight class
        SELECT COUNT(*) INTO promotion_count
        FROM reservation_details rd
        JOIN flight_class fc ON rd.reservation_id = NEW.id AND fc.flight_id = NEW.flight_id
        WHERE fc.promotion_limit > 0;

        IF promotion_count < (SELECT promotion_limit FROM flight_class WHERE flight_id = NEW.flight_id AND class_id = NEW.class_id) THEN
            -- Apply promotion discount
            NEW.discount := (SELECT promotion_discount FROM flight_class WHERE flight_id = NEW.flight_id AND class_id = NEW.class_id);
        END IF;
    END IF;

    -- Rule 3: Verify cancellation is before the cutoff time
    IF NEW.status = 'CANCELLED' THEN
        cutoff_time := flight_record.departure_time - (flight_record.cancellation_cutoff_hours * INTERVAL '1 hour');
        IF NEW.cancellation_date > cutoff_time THEN
            RAISE EXCEPTION 'Cancellation cannot be made after the cutoff time: %', cutoff_time;
        END IF;
    END IF;

    -- Rule 4: Verify the selected flight class corresponds to the flight
    IF NOT EXISTS (
        SELECT 1
        FROM flight_class fc
        WHERE fc.flight_id = NEW.flight_id AND fc.class_id = NEW.class_id
    ) THEN
        RAISE EXCEPTION 'Invalid flight class for the selected flight';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger that fires before insert or update on reservations
DROP TRIGGER IF EXISTS trigger_enforce_reservation_rules ON reservations;
CREATE TRIGGER trigger_enforce_reservation_rules
BEFORE INSERT OR UPDATE ON reservations
FOR EACH ROW
EXECUTE FUNCTION enforce_reservation_rules();