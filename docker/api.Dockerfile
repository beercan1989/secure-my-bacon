FROM maven:3-jdk-8

## Share the local code into the docker
COPY . /code

## Build the project
WORKDIR /code
RUN mvn clean install -DskipTests=true && \
    mkdir /binary && \
    cp -v api/target/api.jar /binary/api.jar && \
    find . -mindepth 1 -delete

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=heroku", "/binary/api.jar"]
