# Getting Started

### Required to run this project
[Java 11](https://www.infoworld.com/article/3514725/installing-oracle-java-se-11-on-ubuntu-18-04.html)
[Maven](https://linuxize.com/post/how-to-install-apache-maven-on-ubuntu-18-04/)

### Creating Intellectus Database and user with granted permissions

1- install postgresql server locally\
2- enter postgresql command line: ```sudo -i -u postgres```\
3- create database: ```create database intellectus_db;```\
4- create user: ```create user intellectus_user with password 'intellectus_pw';```\
5- grant privileges: ```grant all privileges on database intellectus_db to intellectus_user;```
