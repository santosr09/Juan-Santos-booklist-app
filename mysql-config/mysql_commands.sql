#***************************************************

mysql -h localhost -P 3306 -u testuser -ploma1234

mysql -P 3306 -u testuser -ploma1234

mysql -h localhost -P 3306 -u root -ploma1234

mysql -P 3306 -u root -ploma1234

#mysql -h localhost -u testuser -ploma1234 test_books

show GRANTS;

SELECT user, host FROM mysql.user;

CREATE USER 'testuser'@'localhost' IDENTIFIED BY 'loma1234';

#ALTER USER 'testuser'@'localhost' IDENTIFIED BY 'loma1234';

GRANT ALL ON *.* TO 'testuser'@'localhost';
GRANT ALL ON *.* TO 'testuser'@'%';

FLUSH PRIVILEGES;

show databases;

create database test_books;