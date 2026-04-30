FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /build

# Copiamos primero el pom para aprovechar cache de Docker
COPY pom.xml .
COPY src ./src

# Compilamos sin tests para acelerar el build (los tests unitarios van aparte)
RUN mvn -DskipTests package


FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /build/target/*.jar /app/app.jar

EXPOSE 8080

# Se puede sobrescribir desde docker-compose si hace falta
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java","-jar","/app/app.jar"]
