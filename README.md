# RPG Player Management System with Hibernate

This project is an enhanced version of the RPG admin panel from Module 3, now implementing Hibernate for database persistence instead of in-memory storage. The application manages player data for an RPG game through a web interface.

## Prerequisites

- Java 1.8
- Maven
- MySQL 8.0
- IntelliJ IDEA (recommended)

## Dependencies

The project uses the following main dependencies:

```xml
- mysql-connector-java (8.0.30)
- hibernate-core-jakarta (5.6.11.Final)
- p6spy (3.9.1) for SQL query logging
```

## Setup Instructions

1. **Database Setup**
   ```sql
   CREATE SCHEMA `rpg`;
   ```

2. **Project Configuration**
   - Clone/fork the repository
   - Run `mvn clean install` to build the project
   - Configure the application launch in IntelliJ IDEA:
     - Create a new Tomcat configuration
     - Set up the artifact deployment

3. **Application Properties**
   The application uses Hibernate with MySQL. Key configurations:
   - Database URL: `jdbc:mysql://localhost:3306/rpg`
   - Hibernate auto DDL: `update`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/game/
│   │       ├── config/         # Application configuration
│   │       ├── controller/     # REST controllers
│   │       ├── entity/         # Domain entities
│   │       ├── repository/     # Data access layer
│   │       └── service/        # Business logic layer
│   └── resources/
│       ├── hibernate.cfg.xml   # Hibernate configuration
│       ├── init.sql           # Initial data script
│       └── spy.properties     # P6Spy SQL logging configuration
```

## Key Features

1. **Entity Mapping**
   - Player entity mapped to `player` table in `rpg` schema
   - Proper enum handling with `@Enumerated(EnumType.ORDINAL)`
   - Field constraints:
     - Name: max 12 characters
     - Title: max 30 characters
     - All fields are non-nullable

2. **Repository Implementation**
   - `getAllPlayers`: Implemented using Native Query
   - `getAllCount`: Implemented using Named Query
   - Transaction management for data modifications
   - Resource cleanup with `@PreDestroy`

3. **SQL Query Logging**
   - P6Spy integration for query monitoring
   - Logs prepared statements and executed queries with parameters

## Testing

1. Run the application
2. Execute `init.sql` script through MySQL Workbench to populate initial data
3. Access the web interface and verify CRUD operations

## Development Notes

- The repository implementation (`PlayerRepositoryDB`) uses `SessionFactory` for Hibernate operations
- You can switch between memory and DB storage by modifying the `@Qualifier` annotation in `PlayerService`
- For debugging, check console logs for SQL queries through P6Spy

## Switching Storage Implementations

To switch between memory and database storage:
1. Locate `PlayerService` class
2. Modify the `@Qualifier` parameter:
   - Use "memory" for in-memory storage
   - Use "db" for database storage

## SQL Logging Configuration

The `spy.properties` configuration enables detailed SQL logging:
```properties
driverlist=com.mysql.cj.jdbc.Driver
dateformat=yyyy-MM-dd hh:mm:ss a
appender=com.p6spy.engine.spy.appender.StdoutLogger
logMessageFormat=com.p6spy.engine.spy.appender.MultiLineFormat
```

## Note

This implementation uses basic Hibernate integration. For a more Spring-oriented approach to Hibernate integration, refer to Module 5 content.
