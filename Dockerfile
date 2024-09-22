FROM gcr.io/distroless/java21-debian12@sha256:e4cb46a49683df2fd5a93bc669f0c56942d75ea6d08b08f506cc70ca686c5e57

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]