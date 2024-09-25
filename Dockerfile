FROM gcr.io/distroless/java21-debian12@sha256:4ef80b38c61881bdd4d682df9989a9816f4926f8fb41eaaf55d54a6affe6a6c2

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]