FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

CMD["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=railway"]


EXPOSE 8082

