DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS customer_item;

CREATE TABLE IF NOT EXISTS customer (
    id INT GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(250) NOT NULL,
    last_name VARCHAR(250),
    department VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY(id)
    );
    
CREATE TABLE IF NOT EXISTS address (
    id INT GENERATED ALWAYS AS IDENTITY,
    street VARCHAR(250),
    house_number VARCHAR(250),
    zip INT,
    city_name VARCHAR(100),
    fk_address_id INT,
    PRIMARY KEY(id),
    CONSTRAINT fk_address_id
        FOREIGN KEY(fk_address_id) 
            REFERENCES customer(id)
    );

CREATE TABLE IF NOT EXISTS customer_item (
    id INT GENERATED ALWAYS AS IDENTITY,
    item VARCHAR(250),
    fk_customer_id INT,
    PRIMARY KEY(id),
    CONSTRAINT fk_customer_id
        FOREIGN KEY(fk_customer_id) 
            REFERENCES customer(id)
);    