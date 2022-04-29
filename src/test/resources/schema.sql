CREATE TABLE IF NOT EXISTS transaction
(
    tid BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    transaction_total DECIMAL(5, 2) NOT NULL,
    regretted VARCHAR(255)
);