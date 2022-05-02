CREATE TABLE IF NOT EXISTS category
(
    cid BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL,
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
    tid BIGINT PRIMARY KEY AUTO_INCREMENT,
    wid BIGINT NOT NULL,
    cid BIGINT NOT NULL,
    expense_name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    expense_total DECIMAL(5, 2) NOT NULL,
    expense_date DATE NOT NULL,
    regretted boolean  default false,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    FOREIGN KEY ( cid ) REFERENCES category( cid ),
    FOREIGN KEY ( wid ) REFERENCES wallet( wid )

    );

CREATE TABLE IF NOT EXISTS income
(
    iid BIGINT PRIMARY KEY AUTO_INCREMENT,
    wid BIGINT NOT NULL,
    income_date DATE NOT NULL,
    income_total DECIMAL(5, 2) NOT NULL,
    FOREIGN KEY ( wid ) REFERENCES wallet( wid )

    );


-- drop table income;
-- drop table expense;
-- drop table category;
-- drop table wallet;