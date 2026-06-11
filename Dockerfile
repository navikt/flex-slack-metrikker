FROM gcr.io/distroless/java21-debian13@sha256:46918c99fec3a4fb69c5e6d0679883935997f63ad602165369795039875384b0

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]