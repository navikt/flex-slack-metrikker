FROM gcr.io/distroless/java21-debian12@sha256:c298bfc8c8b1aa3d7b03480dcf52001a90d66d966f6a8d8997ae837d3982be3f

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]