# ---- build ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src src
COPY sql sql
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/one-global-devices-api-0.0.1-SNAPSHOT.jar /app/app.jar
COPY sql /app/sql

ENV SERVER_PORT=8080
ENV SPRING_FLYWAY_ENABLED=true
ENV SPRING_FLYWAY_LOCATIONS=filesystem:/app/sql
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
