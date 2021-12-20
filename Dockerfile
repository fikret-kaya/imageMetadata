FROM openjdk:11
LABEL MAINTAINER="<Fikret KAYA> fikret.ky93@gmail.com"
WORKDIR /app
COPY target/imagemetadata-*.jar app.jar
COPY data /app/data

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]