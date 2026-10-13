# EduPulse backend

This is the Java Spring Boot backend for the EduPulse academic management system. It provides JWT-protected REST APIs, role-based access, database persistence, and Flyway migration management.

## Local configuration

The project uses MySQL on localhost with the local dev settings in `src/main/resources/application.properties`.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/edupulse_db
spring.datasource.username=root
spring.datasource.password=admin
```

## Development profile

The application loads the `dev` profile by default. That profile seeds demo accounts in a development-only mode.

## Demo accounts

- Admin: `admin` / `Admin@123`
- Faculty: `faculty` / `Faculty@123`
- Student: `student` / `Student@123`

## Run locally

```powershell
cd .\backend
mvn spring-boot:run
```

## Test locally

```powershell
cd .\backend
mvn test
```
