CREATE TABLE IF NOT EXISTS category
(
    cid BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL

    );

CREATE TABLE IF NOT EXISTS transaction
(
    tid BIGINT PRIMARY KEY AUTO_INCREMENT,
    cid BIGINT NOT NULL,
    expense_name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    expense_total DECIMAL(5, 2) NOT NULL,
    expense_date DATE NOT NULL,
    regretted boolean  default false,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    FOREIGN KEY ( cid ) REFERENCES category( cid )
    );

