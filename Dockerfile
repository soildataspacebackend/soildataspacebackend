FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copiar pom.xml y descargar dependencias (cacheable)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el proyecto
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- Etapa 2: Runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el jar generado desde la etapa anterior
COPY --from=builder /app/target/demo.jar /app/api-v1.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/api-v1.jar"]