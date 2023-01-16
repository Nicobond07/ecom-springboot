-- -- DROP TABLE IF EXISTS sale CASCADE;
-- -- DROP TABLE IF EXISTS product CASCADE;
-- -- DROP TABLE IF EXISTS category CASCADE;
-- -- DROP TABLE IF EXISTS salesman CASCADE;
-- -- DROP TYPE IF EXISTS role CASCADE;
--
-- CREATE TYPE role AS ENUM ('ADMIN', 'SALESMAN');
--
-- CREATE TABLE salesman (
--   id INT AUTO_INCREMENT PRIMARY KEY,
--   pseudo VARCHAR(250) NOT NULL,
--   email VARCHAR(250) NOT NULL,
--   password VARCHAR(250) NOT NULL,
--   promo_code VARCHAR(250) NOT NULL,
--   number_orders INT NOT NULL,
--   earned_cash FLOAT NOT NULL,
--   money_to_send FLOAT NOT NULL,
--   role role,
--   created DATE NOT NULL
-- );
--
-- CREATE TABLE category (
--   id INT AUTO_INCREMENT PRIMARY KEY,
--   name VARCHAR(250) NOT NULL
-- );
--
-- CREATE TABLE product (
--   id INT AUTO_INCREMENT PRIMARY KEY,
--   name VARCHAR(250) NOT NULL,
--   description TEXT NOT NULL,
--   image BLOB NULL,
--   initial_price FLOAT NOT NULL,
--   promo INT NULL,
--   promo_price FLOAT NULL,
--   id_category INT REFERENCES category (id)
-- );
--
-- CREATE TABLE sale (
--   id INT AUTO_INCREMENT PRIMARY KEY,
--   promo_code VARCHAR(250) NOT NULL,
--   total_price FLOAT NOT NULL,
--   created DATE NOT NULL,
--   id_client INT REFERENCES salesman (id),
--   id_product INT REFERENCES product (id)
-- );
--
