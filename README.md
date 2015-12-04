# registration-jdbcrealm

This is a java user registration web application supporting JDBCRealm authorization. It uses MySQL database in the backend. It has a data access layer that is generated with an xml schema utilizing jaxb (JSR 222). The MySQL data access objects implement interfaces for CRUD operations. The web front end uses (JSR 315) Servlet 3/JSP 2.2/EL 2.2/JSTL 1.2 within a Tomcat 7 container. A RESTful interface is implemented with JAX-RS (JSR 311). Many of the resources exposed will work with xml and json content types.  

## Features

Users can create a user account with a username, password and an email. An email will be sent with a url to activate their user account. The data structures support a role based security configuration.

## Dependencies
1. Java 1.7
2. Apache Maven 3.3.1
3. MySQL 5.5.3
4. Apache Tomcat 7.0

## Installation
The following environment is expected:
(Install Java 1.7, Maven 3.3.1 or higher, MySQL 5.5.3, and Apache Tomcat 7.0)

### Clone the repo
````
git clone https://github.com/chrispauley/registration-jdbcrealm.git
cd registration
````

### Database Setup
create database tables using the DDL script in src/main/resources/create_tables_db.sql
create a user account for the registration app.
update the src/main/webapp/WEB-INF/lib/EDITME.database.properties and rename this file to database.properties
This file is necessary for unit testing.

### Server Setup
Add a resoure to the context.xml file in the conf folder
````
<Resource auth="Container" driverClassName="com.mysql.jdbc.Driver" initialSize="1"
logAbandoned="false" maxActive="30" maxIdle="30" maxWait="15000"
name="jdbc/myDatasourceName" password="webuser_password" removeAbandoned="true"
removeAbandonedTimeout="300" type="javax.sql.DataSource" url="jdbc:mysql://localhost:8889/my_database" username="web_useraccount" validationQuery="select now();"/>
````

### Setup the java web application using Maven
mvn generate-sources
mvn install


## Usage

TODO: Write usage instructions

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## History

Originally created in 2012.
Updated to newer containers and versions of the libraries.

## Credits

[Lots of credit goes to the authors of the libraries.]

## License
MIT
