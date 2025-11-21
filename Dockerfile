## Build stage
#FROM eclipse-temurin:21-jdk AS build
#WORKDIR /app
#COPY . .
#RUN chmod +x mvnw
#RUN ./mvnw clean package -DskipTests
#
## Runtime stage
#FROM eclipse-temurin:21-jre
#WORKDIR /app
#COPY --from=build /app/target/*.jar app.jar
#
#EXPOSE 8082
#ENTRYPOINT ["java", "-Dspring.profiles.active=railway", "-jar", "/app/app.jar"]


# Build stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /module-content-tracker-backend
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Runtime
FROM eclipse-temurin:21-jre
WORKDIR /module-content-tracker-backend
COPY --from=build /module-content-tracker-backend/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-Dspring.profiles.active=railway", "-jar", "app.jar"]
