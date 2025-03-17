INSERT INTO roles (label) VALUES
	('USER'),
	('ADMIN');
INSERT INTO class (class) VALUES
	('ECONOMY'),
	('BUSINESS');

-- Insertion des utilisateurs
INSERT INTO users (email, username, pwd) VALUES
    ('user1@example.com', 'User One', 'password1'),
    ('user2@example.com', 'User Two', 'password2'),
    ('admin@example.com', 'Admin', 'admin123');

-- Insertion des avions
INSERT INTO airplanes (model, total_seats) VALUES
    ('Boeing 737', 150),
    ('Airbus A320', 180),
    ('Boeing 787', 250);

-- Insertion des pays
INSERT INTO country (name) VALUES
    ('France'),
    ('United States'),
    ('Japan');

-- Insertion des villes
INSERT INTO cities (name, country_id) VALUES
    ('Paris', 1),
    ('New York', 2),
    ('Tokyo', 3);

-- Insertion des vols
INSERT INTO flights (flight_number, departure_time, arrival_time, reservation_cutoff_hours, cancellation_cutoff_hours, airplane_id, departure_city_id, arrival_city_id) VALUES
    ('FL123', '2023-12-01 08:00:00', '2023-12-01 12:00:00', 24, 12, 1, 1, 2),
    ('FL456', '2023-12-02 10:00:00', '2023-12-02 15:00:00', 24, 12, 2, 2, 3),
    ('FL789', '2023-12-03 14:00:00', '2023-12-03 18:00:00', 24, 12, 3, 3, 1);

-- Insertion des classes de vol
INSERT INTO flight_class (flight_id, class_id, promotion_limit, promotion_discount) VALUES
    (1, 1, 10, 10.00), -- ECONOMY class for flight 1 with a promotion limit of 10 and 10% discount
    (1, 2, 5, 20.00),  -- BUSINESS class for flight 1 with a promotion limit of 5 and 20% discount
    (2, 1, 15, 5.00),  -- ECONOMY class for flight 2 with a promotion limit of 15 and 5% discount
    (2, 2, 8, 15.00),  -- BUSINESS class for flight 2 with a promotion limit of 8 and 15% discount
    (3, 1, 20, 0.00),  -- ECONOMY class for flight 3 with no promotion
    (3, 2, 10, 10.00); -- BUSINESS class for flight 3 with a promotion limit of 10 and 10% discount

-- Insertion des types de passagers
INSERT INTO passenger_type (type_name) VALUES
    ('ADULT'),
    ('CHILD');

-- Insertion des tarifs
INSERT INTO fares (flight_id, passenger_type_id, base_price) VALUES
    (1, 1, 500.00), -- ADULT fare for flight 1
    (1, 2, 250.00), -- CHILD fare for flight 1
    (2, 1, 600.00), -- ADULT fare for flight 2
    (2, 2, 300.00), -- CHILD fare for flight 2
    (3, 1, 700.00), -- ADULT fare for flight 3
    (3, 2, 350.00); -- CHILD fare for flight 3

-- Insertion des tarifs de configuration
INSERT INTO config_fares (passenger_type_id, price) VALUES
    (1, 500.00), -- Default ADULT fare
    (2, 250.00); -- Default CHILD fare