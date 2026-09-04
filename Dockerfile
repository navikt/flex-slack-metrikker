FROM gcr.io/distroless/java21-debian13@sha256:27d6932e85923aa9baf382f3daed5a587fe764c4c5397a0fa085a3f1b8f637ec

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]