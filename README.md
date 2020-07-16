# Getting Started

### Required to run this project
- [Java 11](https://www.infoworld.com/article/3514725/installing-oracle-java-se-11-on-ubuntu-18-04.html)
- [Maven](https://linuxize.com/post/how-to-install-apache-maven-on-ubuntu-18-04/)

### Creating Intellectus Database and user with granted permissions

1. Install postgresql server locally
1. Access to postgres user: ```sudo -i -u postgres```
1. Access to PostgreSQL's command line: ```psql```
1. Create database: ```create database intellectus_db;```
1. Create user: ```create user intellectus_user with password 'intellectus_pw';```
1. Grant privileges: ```grant all privileges on database intellectus_db to intellectus_user;```
