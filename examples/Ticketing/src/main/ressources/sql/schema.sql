CREATE TABLE ville(
   id SMALLSERIAL,
   label VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE type_siege(
   id SMALLSERIAL,
   label VARCHAR(50) ,
   PRIMARY KEY(id)
);

CREATE TABLE modele(
   id SMALLSERIAL,
   label VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE g_reservation(
   id SMALLSERIAL,
   label VARCHAR(50)  NOT NULL,
   valeur SMALLINT,
   PRIMARY KEY(id)
);

CREATE TABLE role(
   id SMALLSERIAL,
   label VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE avion(
   id SMALLSERIAL,
   nom VARCHAR(50) ,
   dt_fabrication DATE NOT NULL,
   modele_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(modele_id) REFERENCES modele(id)
);

CREATE TABLE vol(
   id SERIAL,
   date_vol TIMESTAMP NOT NULL,
   last_reservation INTEGER,
   last_annulation_reservation SMALLINT,
   statue SMALLINT NOT NULL,
   ville_arrive_id INTEGER NOT NULL,
   ville_depart_id INTEGER NOT NULL,
   avion_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(ville_arrive_id) REFERENCES ville(id),
   FOREIGN KEY(ville_depart_id) REFERENCES ville(id),
   FOREIGN KEY(avion_id) REFERENCES avion(id)
);

CREATE TABLE siege_avion(
   id SERIAL,
   nb_siege SMALLINT,
   avion_id INTEGER NOT NULL,
   type_siege_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(avion_id) REFERENCES avion(id),
   FOREIGN KEY(type_siege_id) REFERENCES type_siege(id)
);

CREATE TABLE promotion_reservation(
   id SERIAL,
   nb_siege INTEGER NOT NULL,
   off NUMERIC(10,2)  ,
   type_siege_id INTEGER NOT NULL,
   vol_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(type_siege_id) REFERENCES type_siege(id),
   FOREIGN KEY(vol_id) REFERENCES vol(id)
);

CREATE TABLE prix_siege(
   id SERIAL,
   prix NUMERIC(15,2)   NOT NULL,
   vol_id INTEGER NOT NULL,
   type_siege_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(vol_id) REFERENCES vol(id),
   FOREIGN KEY(type_siege_id) REFERENCES type_siege(id)
);

CREATE TABLE users(
   id SERIAL,
   email VARCHAR(50) ,
   pwd VARCHAR(50) ,
   role_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(role_id) REFERENCES role(id)
);

CREATE TABLE reservation(
   id SERIAL,
   dt_reservation TIMESTAMP NOT NULL,
   statue SMALLINT,
   user_id INTEGER NOT NULL,
   vol_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(user_id) REFERENCES users(id),
   FOREIGN KEY(vol_id) REFERENCES vol(id)
);




