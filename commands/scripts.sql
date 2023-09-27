USE test_books;

CREATE TABLE users (
  id bigint NOT NULL AUTO_INCREMENT,
  first_name varchar(30) DEFAULT NULL,
  last_name varchar(30) DEFAULT NULL,
  country varchar(50) DEFAULT NULL,
  registration_date varchar(10) DEFAULT NULL,
  username varchar(30) DEFAULT NULL,
  password varchar(30) DEFAULT NULL,
  
  PRIMARY KEY (id),
  INDEX (id)
) ENGINE=InnoDB;

CREATE TABLE books (
  id bigint NOT NULL AUTO_INCREMENT,
  author varchar(50) DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  isbn varchar(13) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  published_date varchar(10) DEFAULT NULL,
  publisher varchar(50) DEFAULT NULL,
  
  PRIMARY KEY (id),
  INDEX (id),
  
  FOREIGN KEY (user_id)
    REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE categories (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE user_books (
  user_id bigint NOT NULL,
  book_id bigint NOT NULL,
  
  PRIMARY KEY (user_id, book_id),
  INDEX (user_id, book_id),
  
  FOREIGN KEY (user_id)
	REFERENCES users(id),
    
  FOREIGN KEY (book_id)
	REFERENCES books(id)
) ENGINE=InnoDB;

CREATE TABLE books_categories (
  book_id bigint NOT NULL,
  category_id bigint NOT NULL,
  
  PRIMARY KEY (book_id, category_id),
  INDEX (book_id, category_id),
  
  FOREIGN KEY (book_id)
	REFERENCES books(id),

  FOREIGN KEY (category_id)
	REFERENCES categories(id)
) ENGINE=InnoDB;



# INSERT USERS

INSERT INTO users
(first_name,
last_name,
country,
registration_date,
username,
password)
VALUES
('Julio',
'Gil',
'Mexico',
'2022/01/07',
'jgil',
'jgil1234$');

INSERT INTO users
(first_name,
last_name,
country,
registration_date,
username,
password)
VALUES
('Kelly',
'Smith',
'USA',
'2022/01/05',
'ksmith',
'ksmith1234');

INSERT INTO users
(first_name,
last_name,
country,
registration_date,
username,
password)
VALUES
('Mary',
'Ros',
'Mexico',
'2022/01/06',
'mros',
'mros1234%');


# INSERT BOOKS

INSERT INTO books
(author,
description,
isbn,
name,
published_date,
publisher)
VALUES(
'Juan Villa',
'',
'1234567891235',
'Revelaciones',
'2000/05/18',
'Omega');


INSERT INTO books
(author,
description,
isbn,
name,
published_date,
publisher)
VALUES(
'Rod Johnson',
'',
'1234562391235',
'New Spring',
'2003/03/18',
'Jvm');


#  INSERT Categories

INSERT INTO categories
(name)
VALUES
('engineering');

INSERT INTO categories
(name)
VALUES
('drama');

INSERT INTO categories
(name)
VALUES
('computing');

INSERT INTO categories
(name)
VALUES
('sci-fi');


