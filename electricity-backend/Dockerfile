FROM openjdk:11-jdk

ADD . /electricApp
WORKDIR /electricApp

RUN ./gradlew clean build -i

FROM openjdk:11-jdk

MAINTAINER Bartlomiej Zawadzki

VOLUME /tmp

COPY --from=0 "/electricApp/build/libs/electricity-*-SNAPSHOT.jar" app.jar

CMD [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
