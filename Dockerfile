FROM gcr.io/distroless/java21-debian12:nonroot
WORKDIR /app
COPY target/okonomimodell-api-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
