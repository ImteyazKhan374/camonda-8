# Camunda 8 + Spring Boot Workflow Engine

## 📋 Table of Contents
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Architecture](#-architecture)  
- [Configuration](#%EF%B8%8F-configuration)
- [Troubleshooting](#-troubleshooting)
- [Project Structure](#-project-structure)
- [FAQ](#-faq)
- [Resources](#-resources)

---

## 📦 Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- Java 17+
- Maven or Gradle
- Minimum 4GB RAM allocated to Docker

---

## 🚀 Quick Start

### 1. Start Camunda Services

```bash
docker-compose up -d
```

**Services Overview:**

| Service        | URL                          | Credentials (if auth enabled) |
|----------------|------------------------------|-------------------------------|
| Zeebe Broker   | `localhost:26500` (gRPC)     | -                             |
| Operate        | [http://localhost:8081](http://localhost:8081) | `demo/demo`              |
| Tasklist       | [http://localhost:8082](http://localhost:8082) | `demo/demo`              |
| Elasticsearch  | [http://localhost:9200](http://localhost:9200) | -                         |

---

### 2. Run Spring Boot App

```bash
mvn spring-boot:run
# OR for Gradle
./gradlew bootRun
```

---

### 3. Deploy a BPMN Process

Place your `.bpmn` files inside `src/main/resources`.

**Java Example:**

```java
zeebeClient.newDeployResourceCommand()
    .addResourceFromClasspath("process.bpmn")
    .send()
    .join();
```

---

## 🏗️ Architecture

- **Camunda 8**: Workflow engine powered by Zeebe.
- **Spring Boot**: Hosts Zeebe job workers and business logic.
- **Elasticsearch**: Stores process data for Operate and Tasklist.
- **Operate/Tasklist**: UIs for monitoring and human tasks.

```
[ Client ] → [ Spring Boot Workers ] ⇄ [ Zeebe Broker ] ⇄ [ Operate, Tasklist ]
                                              ⇅
                                         [ Elasticsearch ]
```

---

## ⚙️ Configuration

### 🔑 Key Files

- `docker-compose.yml` – Defines Camunda services
- `application.properties` – Spring Boot Zeebe client config

```properties
# Zeebe Connection
zeebe.client.broker.gateway-address=localhost:26500
zeebe.client.security.plaintext=true

# Worker Settings
zeebe.client.worker.defaultName=spring-worker
zeebe.client.worker.defaultType=order-handler
```

### 🌍 Environment Variables

| Variable | Example Value | Description |
|----------|----------------|-------------|
| `ZEEBE_BROKER_NETWORK_HOST` | `0.0.0.0` | Zeebe bind address |
| `CAMUNDA_OPERATE_ELASTICSEARCH_URL` | `http://elasticsearch:9200` | Operate-ES connection |

---

## 🛠 Troubleshooting

### 1. Zeebe Connection Issues

**Symptom:**
```
INTERNAL: Encountered end-of-stream mid-frame
```

**Fix:**
```bash
docker logs camunda-zeebe
docker-compose restart zeebe
```

---

### 2. Elasticsearch Health Errors

**Symptom:** Operate/Tasklist won't start

**Fix:**
```bash
docker-compose down -v
docker-compose up -d
```

---

### 3. Spring Bean Conflicts

**Symptom:**
```
BeanDefinitionOverrideException: zeebeClient
```

**Fix Options:**
- Remove duplicate `@Bean`
- OR allow overriding:
```properties
spring.main.allow-bean-definition-overriding=true
```

---

### 4. Docker API Errors

**Symptom:**
```
500 Internal Server Error for API route
```

**Fix:**
- Reset Docker Desktop to factory defaults
- Restart Docker

---

## 📂 Project Structure

```
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── tcs/
│   │   │           ├── config/         # Zeebe client config
│   │   │           ├── workers/        # Job handlers
│   │   │           └── Application.java
│   │   └── resources/
│   │       ├── processes/              # BPMN diagrams
│   │       └── application.properties
├── docker-compose.yml                  # Camunda stack
└── README.md
```

---

## ❓ FAQ

**Q: How to check if Zeebe is healthy?**

```bash
docker exec camunda-zeebe zbctl status --insecure
```

---

**Q: Where are process instances stored?**

- In Elasticsearch (`http://localhost:9200/zeebe-*`)

---

**Q: How to scale workers?**

Set in `application.properties`:

```properties
zeebe.client.worker.threads=2
```

---

## 📚 Resources

- [Camunda Official Documentation](https://docs.camunda.io/docs/)
- [Zeebe Java Client API Docs](https://docs.camunda.io/docs/apis-clients/java-client/)
- [Spring Zeebe Starter](https://github.com/camunda-community-hub/spring-zeebe)