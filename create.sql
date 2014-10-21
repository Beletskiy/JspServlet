DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS ROLE;

CREATE TABLE IF NOT EXISTS ROLE
(
    ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    NAME VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS USER
(
    ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ROLE_ID BIGINT NOT NULL,
    LOGIN VARCHAR_IGNORECASE(50) NOT NULL UNIQUE ,
    PASSWORD VARCHAR(50) NOT NULL,
    EMAIL VARCHAR(100) NOT NULL UNIQUE ,
    FIRST_NAME VARCHAR(50),
    LAST_NAME VARCHAR(50),
    BIRTHDAY DATE,
    FOREIGN KEY (ROLE_ID) REFERENCES ROLE (ID)
);

INSERT INTO role VALUES(1, 'admin');
INSERT INTO role VALUES(2, 'user');

INSERT INTO user VALUES(1, 1, 'admin', 'admin', 'admin@gmail.com', 'Alexander', 'Alexandrov', '1970-06-06');
INSERT INTO user VALUES(2, 1, 'super_admin', 'super_admin', 'super_admin@gmail.com', 'Alexey', 'Alexeev', '1980-03-03');
INSERT INTO user VALUES(3, 2, 'user1', 'user1', 'user1@gmail.com', 'Ivan', 'Ivanov', '1990-07-07');
INSERT INTO user VALUES(4, 2, 'user2', 'user2', 'user2@gmail.com', 'Petr', 'Petrov', '1991-08-08');
INSERT INTO user VALUES(5, 2, 'user3', 'user3', 'user3@gmail.com', 'Max', 'Mirniy', '1992-09-09');