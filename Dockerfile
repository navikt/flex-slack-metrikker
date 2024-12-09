FROM gcr.io/distroless/java21-debian12@sha256:903d5ad227a4afff8a207cd25c580ed059cc4006bb390eae65fb0361fc9724c3

ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"

COPY build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]