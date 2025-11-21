FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar


ENTRYPOINT ["./mvnw", "-Dspring.profiles.active=railway", "-jar", "/app.jar"]

EXPOSE 8082
