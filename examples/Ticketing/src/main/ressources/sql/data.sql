INSERT INTO ville (label) VALUES
      ('Antananarivo'),
      ('Paris'),
      ('New York'),
      ('Tokyo'),
      ('Londres'),
      ('Dubaï'),
      ('Johannesburg'),
      ('Sydney'),
      ('Los Angeles'),
      ('Singapour');

INSERT INTO type_siege (label) VALUES
       ('Economique'),
       ('Business');

INSERT INTO modele (label) VALUES
       ('Boeing 737'),
       ('Boeing 747'),
       ('Boeing 777'),
       ('Boeing 787 Dreamliner'),
       ('Airbus A320'),
       ('Airbus A330'),
       ('Airbus A350'),
       ('Airbus A380'),
       ('Embraer E190'),
       ('Bombardier CRJ900');

INSERT INTO g_reservation (label,valeur) VALUES
        ('Last Reservation',3),
        ('Annulation Reservation',8);


INSERT INTO role (label) VALUES
        ('client'),
        ('admin');


INSERT INTO avion (nom, dt_fabrication, modele_id) VALUES
       ('Boeing 737 - Sky Explorer', '2015-06-12', 1),
       ('Boeing 737 - Air Horizon', '2020-08-20', 1),
       ('Boeing 737 - Cloud Hopper', '2013-04-13', 1),
       ('Boeing 737 - Skyline Voyager', '2018-08-06', 1),
       ('Boeing 747 - Ocean Wings', '2005-03-25', 2),
       ('Boeing 747 - Global Cruiser', '2018-11-15', 2),
       ('Boeing 747 - Grand Explorer', '2000-02-23', 2),
       ('Boeing 747 - Sky Monarch', '2012-07-29', 2),
       ('Boeing 777 - Star Voyager', '2016-04-10', 3),
       ('Boeing 777 - Cloud Breaker', '2022-07-30', 3),
       ('Boeing 777 - Sky Seeker', '2011-05-11', 3),
       ('Boeing 777 - Horizon Chaser', '2017-10-22', 3),
       ('Boeing 787 Dreamliner - Dream Sky', '2019-09-14', 4),
       ('Boeing 787 Dreamliner - Horizon Glide', '2021-12-05', 4),
       ('Boeing 787 Dreamliner - Celestial Wings', '2022-08-15', 4),
       ('Boeing 787 Dreamliner - Stellar Flight', '2018-06-30', 4),
       ('Airbus A320 - Blue Jet', '2017-02-22', 5),
       ('Airbus A320 - Solar Breeze', '2023-03-10', 5),
       ('Airbus A320 - Wind Rider', '2012-10-08', 5),
       ('Airbus A320 - Sky Pioneer', '2023-06-01', 5),
       ('Airbus A330 - Silver Falcon', '2014-05-28', 6),
       ('Airbus A330 - Storm Chaser', '2018-07-19', 6),
       ('Airbus A330 - Aero Voyager', '2017-09-05', 6),
       ('Airbus A350 - Cloud Dancer', '2019-12-01', 7),
       ('Airbus A350 - Sky Titan', '2021-06-09', 7),
       ('Airbus A380 - Grand Soarer', '2010-11-21', 8),
       ('Airbus A380 - Sky Emperor', '2013-08-18', 8),
       ('Embraer E190 - Rapid Flyer', '2016-03-04', 9),
       ('Embraer E190 - Horizon Swift', '2021-06-09', 9),
       ('Bombardier CRJ900 - Sky Runner', '2015-01-30', 10);

INSERT INTO siege_avion (nb_siege, avion_id, type_siege_id) VALUES
        (140, 1, 1), (20, 1, 2),  -- Boeing 737 - Sky Explorer
        (140, 2, 1), (20, 2, 2),  -- Boeing 737 - Air Horizon
        (140, 3, 1), (20, 3, 2),  -- Boeing 737 - Cloud Hopper
        (140, 4, 1), (20, 4, 2),  -- Boeing 737 - Skyline Voyager

        (350, 5, 1), (50, 5, 2),  -- Boeing 747 - Ocean Wings
        (350, 6, 1), (50, 6, 2),  -- Boeing 747 - Global Cruiser
        (350, 7, 1), (50, 7, 2),  -- Boeing 747 - Grand Explorer
        (350, 8, 1), (50, 8, 2),  -- Boeing 747 - Sky Monarch

        (340, 9, 1), (56, 9, 2),  -- Boeing 777 - Star Voyager
        (340, 10, 1), (56, 10, 2),  -- Boeing 777 - Cloud Breaker
        (340, 11, 1), (56, 11, 2),  -- Boeing 777 - Sky Seeker
        (340, 12, 1), (56, 12, 2),  -- Boeing 777 - Horizon Chaser

        (250, 13, 1), (46, 13, 2),  -- Boeing 787 Dreamliner - Dream Sky
        (250, 14, 1), (46, 14, 2),  -- Boeing 787 Dreamliner - Horizon Glide
        (250, 15, 1), (46, 15, 2),  -- Boeing 787 Dreamliner - Celestial Wings
        (250, 16, 1), (46, 16, 2),  -- Boeing 787 Dreamliner - Stellar Flight

        (160, 17, 1), (20, 17, 2),  -- Airbus A320 - Blue Jet
        (160, 18, 1), (20, 18, 2),  -- Airbus A320 - Solar Breeze
        (160, 19, 1), (20, 19, 2),  -- Airbus A320 - Wind Rider
        (160, 20, 1), (20, 20, 2),  -- Airbus A320 - Sky Pioneer

        (230, 21, 1), (47, 21, 2),  -- Airbus A330 - Silver Falcon
        (230, 22, 1), (47, 22, 2),  -- Airbus A330 - Storm Chaser
        (230, 23, 1), (47, 23, 2),  -- Airbus A330 - Aero Voyager

        (270, 24, 1), (55, 24, 2),  -- Airbus A350 - Cloud Dancer
        (270, 25, 1), (55, 25, 2),  -- Airbus A350 - Sky Titan

        (550, 26, 1), (65, 26, 2),  -- Airbus A380 - Grand Soarer
        (550, 27, 1), (65, 27, 2),  -- Airbus A380 - Sky Emperor

        (90, 28, 1), (10, 28, 2),  -- Embraer E190 - Rapid Flyer
        (90, 29, 1), (10, 29, 2),  -- Embraer E190 - Horizon Swift

        (80, 30, 1), (10, 30, 2);  -- Bombardier CRJ900 - Sky Runner

INSERT INTO users (email, pwd, role_id) VALUES
        ('admin@example.com', 'admin123', 1), -- Admin
        ('client1@example.com', 'client123', 2), -- Client 1
        ('client2@example.com', 'client123', 2), -- Client 2
        ('client3@example.com', 'client123', 2), -- Client 3
        ('client4@example.com', 'client123', 2); -- Client 4

