# APA-CRM — Main-Service

Main-Service is the core Spring Boot microservice of the APA-CRM platform that implements organizational and CRM domain behavior used by the rest of the system. It exposes REST endpoints, orchestrates calls to other internal services (Auth-Service, etc.), performs persistence of CRM entities, and publishes domain events for asynchronous processing.

---

## Table of contents

- Project summary
- Technology stack
- Configuration & environment
- Authentication & integration
- Testing
- Pull Requests
- Issues
- Code of Conduct
- License
- Contact & Support

---

## Project summary

Main-Service implements organization-related domain logic, user/organization membership handling, role/permission integration with the Auth-Service, and event publishing for cross-service communication. The repository is a Java 21 Spring Boot application that uses Spring Data JPA for persistence and Spring Cloud OpenFeign for inter-service HTTP integration.

---

## Technology stack

- Java 21
- Spring Boot 3.x (parent POM)
- Spring Data JPA
- Spring Web (REST controllers)
- Spring Cloud OpenFeign (feign clients)
- Spring Cloud Netflix Eureka (Eureka client)
- Spring AMQP (RabbitMQ) for messaging
- MapStruct for DTO mapping
- Lombok
- Runtime DB driver: PostgreSQL
- Build: Maven (mvnw wrapper present)
- Container: Docker (Dockerfile present)


---

## Configuration & environment

Main-Service reads configuration from standard Spring Boot sources (application.yml/properties, environment variables). Key environment properties:

  - DISCOVERY_HOST
  - DISCOVERY_PORT
  - MAIN_DATASOURCE_HOST
  - MAIN_DATASOURCE_PORT
  - MAIN_DATASOURCE_DATABASE
  - MAIN_DATASOURCE_USERNAME
  - MAIN_DATASOURCE_PASSWORD
  - RABBIT_HOST
  - RABBIT_PORT
  - RABBIT_USERNAME
  - RABBIT_PASSWORD

Look for application*.yml/properties in src/main/resources for concrete keys (scan may be incomplete; verify files in the repo). 
See [application.properties](https://github.com/APA-CRM/Main-Service/blob/develop/src/main/resources/application.properties)

---

## Authentication & integration

- The service uses a Feign client to call Auth-Service for user and role information (see [AuthClient](https://github.com/APA-CRM/Main-Service/blob/develop/src/main/java/com/crm/main/feign/AuthClient.java)).
- JWT-backed, role-aware authorization is expected to be enforced at the gateway.
- When running locally, point Auth-Service/Gateway endpoints to your local instances or set up a mock for integration tests.

---

## Testing

- Unit and integration test setup: look for tests in src/test (test tree is present).
- Some integration tests may require running dependent services (Auth-Service, DB, RabbitMQ).
- The POM includes spring-boot-starter-test and H2 for test scope; tests can be executed with `./mvnw test`.

---

### Pull Requests

- Open a Pull Request (PR) against the `develop` branch.
- Fill out the PR template with context about your change.
- Ensure your PR passes CI/CD checks (see workflows in `.github/`).
- Respond to code review feedback promptly.

---

### Issues

- Use [GitHub Issues](https://github.com/APA-CRM/Main-Service/issues) for bugs or feature requests.
- Please include reproduction steps and environment details for bugs.

---

### Code of Conduct

- Be respectful and constructive in issues, PRs, and discussions.
- Follow the [Contributor Covenant](https://www.contributor-covenant.org/) where applicable.

---

## License

This project is licensed under the terms found in the [LICENSE](./LICENSE) file.

---

## Contact & Support

For help or questions, please open an issue or contact the maintainers via GitHub.

---
