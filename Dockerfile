FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

#ENV DB_URL=DB접속주소/db
#ENV DB_USERNAME=DB계정
#ENV DB_PASSWORD=DB패스워드

# JAR 파일을 실행합니다.
ENTRYPOINT ["java", "-Xms512m", "-Xmx768m", "-XX:+UseG1GC", "-jar", "app.jar"]

## docker build --platform linux/amd64 -t godligefram:0.0.7 .
## docker run -d -p 8084:8080 godligefram:0.0.7

