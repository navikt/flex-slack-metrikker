FROM gcr.io/distroless/java21-debian12@sha256:ed87b011df38601c55503cb24a0d136fed216aeb3bcd57925719488d93d236f4

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]