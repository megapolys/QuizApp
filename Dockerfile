FROM bellsoft/liberica-openjdk-alpine-musl:20.0.1
COPY /target/quiz-app.jar /app/quiz-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/quiz-app.jar"]
