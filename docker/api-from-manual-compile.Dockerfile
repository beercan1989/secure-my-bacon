FROM java:8-jre-alpine
LABEL maintainer james@baconi.co.uk

ADD api/target/api.jar /opt/secure-my-bacon/api.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=heroku,docker", "/opt/secure-my-bacon/api.jar"]
