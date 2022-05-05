
CREATE TABLE IF NOT EXISTS users
(
    uid BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL
    );
CREATE TABLE IF NOT EXISTS category
(
    cid BIGINT PRIMARY KEY AUTO_INCREMENT,
    uid BIGINT NOT NULL,
    category_name VARCHAR(255) NOT NULL,
    category_type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    FOREIGN KEY ( uid ) REFERENCES users( uid ) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS expense
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    uid BIGINT NOT NULL,
    cid BIGINT NOT NULL,
    transaction_description VARCHAR(255) NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_total DECIMAL(5, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    FOREIGN KEY ( cid ) REFERENCES category( cid ) ON DELETE CASCADE,
    FOREIGN KEY ( uid ) REFERENCES users( uid ) ON DELETE CASCADE

    );

CREATE TABLE IF NOT EXISTS income
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    uid BIGINT NOT NULL,
    cid BIGINT NOT NULL,
    transaction_description VARCHAR(255) NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_total DECIMAL(5, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    FOREIGN KEY ( cid ) REFERENCES category( cid ) ON DELETE CASCADE,
    FOREIGN KEY ( uid ) REFERENCES users( uid ) ON DELETE CASCADE

    );



-- drop table income;
-- drop table expense;
-- drop table category;
-- drop table users;