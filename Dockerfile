# BIULD Stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build

COPY .     ./azki-reservation

RUN mvn -B -f azki-reservation/azki-reservation-api/pom.xml clean install -DskipTests=true dependency:go-offline

RUN mvn -B -f azki-reservation/pom.xml clean package -DskipTests=true dependency:go-offline

# RUN Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /build/azki-reservation/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
