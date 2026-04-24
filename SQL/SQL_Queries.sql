-- schema.sql

CREATE TABLE category (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          created_at DATETIME(6),
                          updated_at DATETIME(6),
                          category_description VARCHAR(255),
                          category_title VARCHAR(100) NOT NULL,
                          PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE user (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      created_at DATETIME(6),
                      updated_at DATETIME(6),
                      about VARCHAR(255),
                      email VARCHAR(255),
                      name VARCHAR(100) NOT NULL,
                      password VARCHAR(255),
                      PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE role (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      name VARCHAR(255),
                      PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE post (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      created_at DATETIME(6),
                      updated_at DATETIME(6),
                      add_date DATE,
                      content VARCHAR(10000),
                      image_name VARCHAR(255),
                      title VARCHAR(100) NOT NULL,
                      category_id BIGINT,
                      user_id BIGINT,
                      PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE comment (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         created_at DATETIME(6),
                         updated_at DATETIME(6),
                         content VARCHAR(255),
                         post_id BIGINT,
                         PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE user_role (
                           user BIGINT NOT NULL,
                           role BIGINT NOT NULL,
                           PRIMARY KEY (user, role)
) ENGINE=InnoDB;

-- Foreign Keys

ALTER TABLE comment
    ADD CONSTRAINT FKs1slvnkuemjsq2kj4h3vhx7i1
        FOREIGN KEY (post_id) REFERENCES post(id);

ALTER TABLE post
    ADD CONSTRAINT FKg6l1ydp1pwkmyj166teiuov1b
        FOREIGN KEY (category_id) REFERENCES category(id);

ALTER TABLE post
    ADD CONSTRAINT FK72mt33dhhs48hf9gcqrq4fxte
        FOREIGN KEY (user_id) REFERENCES user(id);

ALTER TABLE user_role
    ADD CONSTRAINT FK26f1qdx6r8j1ggkgras9nrc1d
        FOREIGN KEY (role) REFERENCES role(id);

ALTER TABLE user_role
    ADD CONSTRAINT FKmow7bmkl6wduuutk26ypkgmm1
        FOREIGN KEY (user) REFERENCES user(id);