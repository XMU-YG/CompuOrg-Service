CREATE DATABASE IF NOT EXISTS compuOrg DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

CREATE USER 'compuOrgAdmin'@'localhost' IDENTIFIED BY '123456';
CREATE USER 'compuOrgAdmin'@'%' IDENTIFIED BY '123456';

GRANT ALL ON compuOrg.* TO 'compuOrgAdmin'@'localhost';
GRANT ALL ON compuOrg.* TO 'compuOrgAdmin'@'%';

FLUSH PRIVILEGES;