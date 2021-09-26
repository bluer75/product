# Product API

### Overview

This is simple SpringBoot ​application providing RESTful API for products.
API supports basic CRUD operations:
* Create a new product
* Retrieve a list of non deleted products
* Retrieve a list of deleted products
* Update a non-deleted product
* Delete a non-deleted product (​soft deletion​)

Products are stored in in-memory database (Apache Derby). Note that entities are not persisted after application stops. Each product has following attributes:
* unique id
* name
* price
* date when it was created
* deleted flag indicating soft-deletion (this field is hidden)

Please see Swagger documentation: <http://localhost:8080/swagger-ui.html> for detailed information.

This  project was build/tested with Java 11 and Maven 3.6.3 

### Building
To build the application please execute following command (maven is required): mvn clean install

### Testing
During the build all test are executed and their status is provided in the console.

### Starting
To start the application with embedded Tomcat <http://localhost:8080/products/>, please execute following command (successful build is required): mvn spring-boot:run 

