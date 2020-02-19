DROP TABLE personas IF EXISTS;

CREATE TABLE personas(
  id BIGINT IDENTITY NOT NULL PRIMARY KEY,
  primer_nombre VARCHAR(20),
  segundo_nombre VARCHAR(20),
  telefono VARCHAR(10)
);
