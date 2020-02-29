CREATE SEQUENCE people_id_seq;

CREATE TABLE people  (
											 person_id BIGINT NOT NULL DEFAULT nextval('people_id_seq') PRIMARY KEY,
											 first_name VARCHAR(20),
											 last_name VARCHAR(20)
);

ALTER SEQUENCE people_id_seq
	OWNED BY people.person_id;