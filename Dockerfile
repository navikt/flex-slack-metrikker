FROM gcr.io/distroless/java25-debian13@sha256:2c2450426c449aa19e6fb12a151172187588f6f6fc6d7183b3af559d208a5e8b

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]