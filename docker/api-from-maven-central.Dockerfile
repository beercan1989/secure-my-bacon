FROM java:8-jre-alpine
LABEL maintainer james@baconi.co.uk

ENV MAVEN_GROUP_ID="uk.co.baconi.secure" \
    MAVEN_ARTIFACT_ID="api" \
    MAVEN_VERSION="0.0.1" \
    MAVEN_PACKAGING="war" \
    JAVA_OPTS="-Xms256M -Xmx256M -XX:+UseConcMarkSweepGC"

RUN mkdir -p '/opt/secure-my-bacon' && \
    wget -O '/opt/secure-my-bacon/api.jar' "http://repo1.maven.org/maven2/${MAVEN_GROUP_ID//.//}/${MAVEN_ARTIFACT_ID}/${MAVEN_VERSION}/${MAVEN_ARTIFACT_ID}-${MAVEN_VERSION}.${MAVEN_PACKAGING}"

WORKDIR /opt/secure-my-bacon

ENV JAVA_OPTS="-Xms256m -Xmx256m -XX:+UseConcMarkSweepGC"

EXPOSE 8080

CMD ["sh", "-c", "java ${JAVA_OPTS} -jar api.jar"]
