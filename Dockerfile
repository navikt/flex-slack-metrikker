FROM gcr.io/distroless/java21-debian13@sha256:6cc0aa2d30ed370e4e2ff75091bd547e15f9cf9508796ae21dff331b7c406cd9

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]