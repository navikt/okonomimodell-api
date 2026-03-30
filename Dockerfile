FROM gcr.io/distroless/java21-debian12:nonroot
WORKDIR /app
COPY target/kontoplan-api-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
