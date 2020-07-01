FROM openjdk:8-alpine AS TEMP_BUILD_IMAGE
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
COPY src $APP_HOME/src
RUN ./gradlew build || exit 1
#COPY . .
#RUN ./gradlew build

FROM openjdk:8-alpine
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
ARG ARTIFACT_NAME=demo-0.0.1-SNAPSHOT.jar
ENV APP_HOME /usr/app/
WORKDIR $APP_HOME
#COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/*.jar ./app.jar
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/${ARTIFACT_NAME} app.jar
RUN wget https://github.com/signalfx/signalfx-java-tracing/releases/latest/download/signalfx-tracing.jar -O ./signalfx-tracing.jar
#ENV JAVA_OPTS $JAVA_OPTS -javaagent:./signalfx-tracing.jar -Dsignalfx.service.name=kikeyama_spring
EXPOSE 8080
CMD [ "java", "-javaagent:./signalfx-tracing.jar", "-Dsignalfx.service.name=kikeyama_spring", " -Dsignalfx.logs.injection=true", "-jar", "app.jar" ]
