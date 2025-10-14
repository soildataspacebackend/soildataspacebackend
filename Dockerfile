# Imagen base: Microsoft Build of OpenJDK 21 (versión ligera de Alpine)
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

# Copiar el JAR compilado desde la carpeta target
COPY target/demo.jar /api-v1.jar

# Exponer el puerto que usa Spring Boot
EXPOSE 8080

# Comando por defecto: ejecutar el JAR con Java
ENTRYPOINT ["java", "-jar", "/api-v1.jar"]