CREATE TABLE IF NOT EXISTS category
(
    cid BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL,
    category_type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL

    );

CREATE TABLE IF NOT EXISTS wallet
(
    wid BIGINT PRIMARY KEY AUTO_INCREMENT,
    wallet_name VARCHAR(255) NOT NULL,
    balance DECIMAL(5, 2) NOT NULL default 0,
    valid_from DATE DEFAULT (curdate()),
    valid_until DATE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL

    );

CREATE TABLE IF NOT EXISTS expense
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    wid BIGINT NOT NULL,
    cid BIGINT NOT NULL,
    transaction_description VARCHAR(255) NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_total DECIMAL(5, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    FOREIGN KEY ( cid ) REFERENCES category( cid ),
    FOREIGN KEY ( wid ) REFERENCES wallet( wid )

    );

CREATE TABLE IF NOT EXISTS income
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    wid BIGINT NOT NULL,
    cid BIGINT NOT NULL,
    transaction_description VARCHAR(255) NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_total DECIMAL(5, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    FOREIGN KEY ( cid ) REFERENCES category( cid ),
    FOREIGN KEY ( wid ) REFERENCES wallet( wid )

    );


CREATE TABLE IF NOT EXISTS users
(
    uid BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL
    );

-- drop table income;
-- drop table expense;
-- drop table category;
-- drop table wallet;
-- drop table user;