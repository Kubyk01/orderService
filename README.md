# OrderService

This project is a simple Spring Boot application that manages orders for an e-commerce platform. It includes features such as creating, updating, and deleting orders, as well as providing statistics.

## Prerequisites

Before you can run this project, ensure you have the following installed:

- Java Development Kit (JDK) 23
- Maven 3.x or later

## How to Start the Project

### Option 1: Run with Default Profile (No Endpoints)

1. **Build and Install**: Run the following Maven command in your terminal to build and install the project:

   ```bash
   mvn clean install -DisRunningMaven=true
   ```

2. **Run the Application**: Execute the application using Spring Boot's main class by running:

   ```bash
   java -jar target/orderService-0.0.1-SNAPSHOT.jar
   ```

### Option 2: Run with Endpoint Profile

1. **Build and Install**: Ensure you have the correct profiles set in your Maven configuration or command line. If not, run:

   ```bash
   mvn clean install -DisRunningMaven=true -PEndpoints
   ```

2. **Run the Application**: Start the application with the endpoint profile by executing:

   ```bash
   java -jar target/orderService-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=Endpoints
   ```

### Option 3: Using a Run Configuration in IDE

If you are using an Integrated Development Environment (IDE) like IntelliJ IDEA or Eclipse, you can import configurations from .run folder for both profiles, or create own:

#### IntelliJ IDEA

1. **Maven Build**: Create a Maven build configuration with the -PEndpoints profile to build and install the project.

2. **Spring Boot Application**: Run the Spring Boot application using its main class.

3. **Spring Boot Application with enable endpoints**:Run the Spring Boot application using its main class and set profile as Endpoints
## Endpoints

The application uses several endpoints, including:

- `/orders`: Manage orders (create, retrieve, update, delete).


## Accessing the Application

Once the application is running, you can access it using a web browser or an API client like Postman. The base URL for the application will be `http://localhost:8080`.

- **GET /orders**: Retrieve all orders.
- **POST /orders**: Create a new order.
- **GET /orders/{id}**: Retrieve details of a specific order by ID.
- **PUT /orders/{id}/status**: Update the status of a specific order.
- **PUT /orders/{id}**: Update the details of a specific order.
- **DELETE /orders/{id}**: Delete a specific order.
- **GET /orders/stats** Get statistic information.


## Running Tests

To run the tests for this project, use Maven:

```bash
mvn test -DisRunningMaven=true
```

This will execute all unit and integration tests in your project.

## Notes

- **Database**: The application uses an mongoDB database, all credential is set in application.properties