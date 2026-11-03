FROM gcr.io/distroless/java21-debian13@sha256:1bc4eca3c3b351743be673d7109951b509d6f47a57648e5e2efe784a1b051c4a

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]