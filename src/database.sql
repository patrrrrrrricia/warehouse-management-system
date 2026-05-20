DROP TABLE IF EXISTS bill;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS client;

-- 1. creare baza de date 
CREATE DATABASE IF NOT EXISTS warehousedb;

-- 2. pt server sa lucreze in baza de date
USE warehousedb;

-- 3. creare tabel pt clienti
CREATE TABLE client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(100),
    email VARCHAR(100)
);

-- 4. creare tabel pt produse
CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    stock DOUBLE NOT NULL,
    price DOUBLE NOT NULL
);

-- 5. creare tabel pt comenzi
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    clientId INT,
    productId INT,
    quantity INT
);

-- 6. creare tabel pt facturi
CREATE TABLE bill (
    orderId INT PRIMARY KEY,
    clientName VARCHAR(100),
    productName VARCHAR(100),
    quantity INT,
    totalAmount DOUBLE
);