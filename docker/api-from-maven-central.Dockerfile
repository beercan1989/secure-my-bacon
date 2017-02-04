FROM java:8-jre-alpine
LABEL maintainer james@baconi.co.uk

ENV MAVEN_GROUP_ID="uk.co.baconi.secure" \
    MAVEN_ARTIFACT_ID="api" \
    MAVEN_VERSION="0.0.1" \
    MAVEN_PACKAGING="war"

RUN mkdir -p '/opt/secure-my-bacon' && \
    wget -O '/opt/secure-my-bacon/api.jar' "http://repo1.maven.org/maven2/${MAVEN_GROUP_ID//.//}/${MAVEN_ARTIFACT_ID}/${MAVEN_VERSION}/${MAVEN_ARTIFACT_ID}-${MAVEN_VERSION}.${MAVEN_PACKAGING}"

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=heroku,docker", "/opt/secure-my-bacon/api.jar"]
