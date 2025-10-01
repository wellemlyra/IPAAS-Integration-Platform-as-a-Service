# Etapa 1: build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:21-jre
WORKDIR /work/

# Copia toda a estrutura do fast-jar
COPY --from=build /workspace/target/quarkus-app /work/quarkus-app

# Executa o JAR principal do fast-jar
ENTRYPOINT ["java", "-jar", "quarkus-app/quarkus-run.jar"]