# Используем официальное изображение OpenJDK 21
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем jar файл вашего приложения в контейнер
COPY target/*.jar app.jar
COPY .env .env

# Устанавливаем переменную для работы с Spring Boot
ENV JAVA_OPTS=""

# Открываем порт, на котором будет работать приложение
EXPOSE 8090

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

