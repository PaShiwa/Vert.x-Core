# Docker example using fatjar
# - docker build -t example/learn_vertx .
# - docker run -t -i -p 8888:8888 example/learn_vertx

# https://hub.docker.com/_/adoptopenjdk

# Alternative https://hub.docker.com/_/amazoncorretto
# FROM amazoncorretto:11
FROM openjdk:20

ENV FAT_JAR learn-vertx-1.0.0-SNAPSHOT-fat.jar
ENV APP_HOME /usr/app

EXPOSE 8888

COPY target/$FAT_JAR $APP_HOME/

WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $FAT_JAR"]
