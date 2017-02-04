FROM maven:3-jdk-8-alpine
LABEL maintainer="james@baconi.co.uk"

RUN mkdir -p /opt/src/secure-my-bacon && \
    mkdir -p /opt/secure-my-bacon

WORKDIR /opt/src/secure-my-bacon

ADD . /opt/src/secure-my-bacon

RUN mvn install && \
    cp api/target/api.jar /opt/secure-my-bacon/api.jar && \
    rm -rvf /opt/src/secure-my-bacon && \
    rm -rvf /usr/share/maven/ref/repository

WORKDIR /opt/secure-my-bacon

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=heroku,docker", "api.jar"]
