# Module-Content-Tracker

The module-content-tracker-RAG service enables visualization of module content using Retrieval-Augmented Generation (
RAG) and supports intelligent searching for relevant modules.
It leverages embedding models to vectorize the content and chat models to enable natural language interaction.

## How to install

This section contains information relating to the configuration of the application to be run corectly.

### Prerequisites

- Java 21
- Maven 3.8.8
- Docker 28.0.1
- Docker Compose 2.33.1

### Configuration

The following environment variables are required:

- MISTRAL_AI_TOKEN=wDEdg2xn2aAiKpSnrq7glAiIxpkzzaIz

You can export it in your terminal or add it to your .env file if using one.

Start the required services (Weaviate, PostgreSQL, Grafana)

```
docker compose up -d
```

## How to build

This project is a Maven project. It can be built without profiles.

```
mvn clean install -DskipTests

```

## How to run

Run locally as Spring Boot application with profile **local**.

```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Swagger
-------

Swagger is reachable on the url http://localhost:8082/swagger-ui/index.html

The following endpoints are available:

- /ai/chat: Chatbot function
- /ai/findByKeywords: Semantic module search by keywords

Grafana
-------

To Show Grafana go the url http://localhost:3000/dashboards. You should see the dashboard "Module Content Tracker".
If you do not see it, please fix file permissions:

```
sudo chmod -R 777 grafana
```

If you do not see data yet, wait for the initial analysis to finish (appr 20 min).
