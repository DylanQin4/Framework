BEGIN;

TRUNCATE TABLE
    reservation_passengers,
    reservations,
    flight_class_passenger,
    flights
RESTART IDENTITY
;

COMMIT;
