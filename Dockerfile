FROM gcr.io/distroless/java21-debian12@sha256:cf7fce959603124dcc412bc2d245d84a4e0d86c4c02c6eae2cf13e973c246f24

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/* /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]