FROM java:11-jre-alpine

ADD api/target/api.jar /opt/secure-my-bacon/api.jar

WORKDIR /opt/secure-my-bacon

ENV JAVA_OPTS="-Xms256m -Xmx256m -XX:+UseConcMarkSweepGC"

EXPOSE 8080

CMD ["sh", "-c", "java ${JAVA_OPTS} -jar api.jar"]
