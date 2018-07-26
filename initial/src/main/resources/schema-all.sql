DROP TABLE people IF EXISTS;

CREATE TABLE people  (
    people_id BIGINT  NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    PRIMARY KEY (`people_id`)
);
