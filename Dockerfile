FROM gcr.io/distroless/java21-debian13@sha256:26a517c7f7d69a98adab4d1e71d5a3a9f1079c85ac9c4193ce6b6bd3d73496f3

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]