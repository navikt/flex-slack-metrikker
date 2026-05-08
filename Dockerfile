FROM gcr.io/distroless/java21-debian13@sha256:53ce8e6ab58ff683ec8de330610605a8d0a2d12135d3a1b79fe53290eb1999f2

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]