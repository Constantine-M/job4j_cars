[![CI with Maven](https://github.com/Constantine-M/job4j_cars/actions/workflows/gitActions.yml/badge.svg)](https://github.com/Constantine-M/job4j_cars/actions/workflows/gitActions.yml)


# Project "cars"

About project
------------
This is a car-buying website that lets you pick the car you want from its selection of vehicles.
There are ads in website, that contains an information about car:
- description
- car brand
- car body type
- photos
- The ad has the status - sold or not
You can create, update, delete your ads.

Tech stack.
--------------
- Java 17
- PostgreSQL 14
- Spring Boot 2
- Hibernate 5
- Thymeleaf
- Bootstrap
- Liquibase
- Sql2o
- Lombok
- Querydsl

Environment requirements.
------------------------
1. Java 17
2. Maven 3.8
3. PostgreSQL 14

Launching the project.
---------------------
1. Recommended Chrome based browser.
2. Install PostgreSQL: login - postgres, password - password;
3. Create database;
> CREATE DATABASE cars;
4. Download the source code of Bootstrap 5 and unzip the file
   https://github.com/twbs/bootstrap/archive/v5.3.3.zip
---
5. Сopy the folder to the specified directory
- src/main/resources/static/<bootstrap folder>
---
4. Build the project and run the Spring Boot application
> mvn clean package spring-boot:run

Application Interaction.
------------------------
------------------------
Main
----
![img.png](screenshots/main.png)

- with advanced search

![img.png](screenshots/advancedsearch.png)

Registration
--------
![img.png](screenshots/register.png)

Login
--------
![img.png](screenshots/login.png)

Get detail info
----------
![img.png](screenshots/detail.png)

Add post
---------
![img.png](screenshots/add.png)

Userposts
----------
![img.png](screenshots/userposts.png)

Contacts.
--------
- Telegram - https://t.me/ConstaMezenin.