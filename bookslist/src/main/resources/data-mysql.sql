USE test_books;

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


