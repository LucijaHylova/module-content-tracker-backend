FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /module-content-tracker-backend
COPY --from=build /module-content-tracker-backend/target/*.jar module-content-tracker-backend.jar


ENTRYPOINT ["./mvnw", "-Dspring.profiles.active=railway", "-jar", "/module-content-tracker-backend.jar"]

EXPOSE 8082
