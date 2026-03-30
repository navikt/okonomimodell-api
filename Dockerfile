FROM eclipse-temurin:21-jre-alpine
RUN apk upgrade --no-cache
WORKDIR /app
COPY target/kontoplan-api-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
