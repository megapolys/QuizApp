FROM bellsoft/liberica-openjdk-alpine-musl:20.0.1

# создаем рабочую директорию внутри контейнера
WORKDIR /app

# копируем jar-файл приложения в контейнер
COPY ./app/app.jar /app/app.jar

# создаем папку для загружаемых файлов внутри контейнера
RUN mkdir -p /app/uploads

# приложение внутри контейнера слушает порт 8080
EXPOSE 8080

# запускаем jar-файл
ENTRYPOINT ["java", "-jar", "/app/app.jar"]