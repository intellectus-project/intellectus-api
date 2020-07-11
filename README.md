# Getting Started

### Creating Intellectus Database and user with granted permissions

1- install postgresql server locally\
2- enter postgresql command line: ```sudo -i -u postgres```\
3- create database: ```create database intellectus_db;```\
4- create user: ```create user intellectus_user with password 'intellectus_pw';```\
5- grant privileges: ```grant all privileges on database intellectus_db to intellectus_user;```
