DROP TABLE people;

CREATE TABLE people  (
											 person_id SERIAL NOT NULL PRIMARY KEY,
											 first_name VARCHAR(20),
											 last_name VARCHAR(20)
);
