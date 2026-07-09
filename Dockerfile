FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre@sha256:7f3049eafc632440b1dd3dd92a5daa4f86645c8baec3ef1366f94e91bc3f0a80
ENV TZ="Europe/Oslo"
COPY target/*.jar app.jar
CMD ["-jar","app.jar"]