FROM gcr.io/distroless/java25-debian13@sha256:d72c7b79a8c4babd6a69cf00979c9eed7f87e3e2b7d6871c6a0e9f91ff626cea

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]