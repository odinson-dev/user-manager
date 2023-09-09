# User-Manager Project

## Overview

The User-Manager project is a Java-based application designed to manage user accounts and authorization. The project is organized into multiple subdirectories, each serving a specific purpose. The primary components are:

- `authorization-server`: Handles the authorization logic.
- `account-manager`: Manages user accounts.
- `access-provider`: Provides access control.
- `libs`: Contains libraries used across the project.

## File Structure

- File Extensions: `.java`, `.properties`, `.yml`
- Relevant Subdirectories:
  - `libs`: 100% relevance
  - `authorization-server`: 66% relevance
  - `account-manager`: 37% relevance
  - `access-provider`: 22% relevance

---

## Detailed Components

### Authorization Server

The `authorization-server` directory contains the logic for authorizing users. It is responsible for token generation, validation, and other OAuth2 related functionalities.

#### Key Classes and Methods

- `AuthController`: Handles authentication requests.
  - `authenticate()`: Authenticates a user.
  - `refreshToken()`: Refreshes the authentication token.

### Account Manager

The `account-manager` directory is responsible for managing user accounts. It provides functionalities like registration, profile updates, and account deletion.

#### Key Classes and Methods

- `AccountController`: Manages account-related requests.
  - `register()`: Registers a new user.
  - `updateProfile()`: Updates the user profile.

### Access Provider

The `access-provider` directory contains the logic for access control. It defines roles and permissions and checks whether a user is authorized to perform certain actions.

#### Key Classes and Methods

- `AccessController`: Manages access control.
  - `checkPermission()`: Checks if a user has a specific permission.

### Libraries

The `libs` directory contains common libraries and utilities used across the project. This includes third-party libraries and custom utility classes.

---

## Installation and Setup

1. Clone the repository.
2. Navigate to the project directory.
3. Run `mvn install` to install dependencies.
4. Start the application using `java -jar <jar-file>`.

## API Endpoints

- `/api/auth`: Authentication
- `/api/account`: Account management
- `/api/access`: Access control

## Dependencies

- Spring Boot
- OAuth2
- PostgreSQL

---

## Future Enhancements

- Implement Two-Factor Authentication.
- Add more granular access control.

---


