# Usa la imagen oficial de OpenJDK 17
FROM eclipse-temurin:17-jdk-focal

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR construido al contenedor
COPY target/*.jar app.jar

# Variables de entorno para la base de datos (pueden ser sobreescritas al ejecutar el contenedor)
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/facturacion
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=1234

# Puerto que expone el contenedor
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","app.jar"] 