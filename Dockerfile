# 공통 빌드 스테이지
FROM gradle:8.13-jdk17 AS base-builder
WORKDIR /home/gradle/project

COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

COPY medicall-api-common/build.gradle medicall-api-common/
COPY medicall-api-doctor/build.gradle medicall-api-doctor/
COPY medicall-api-patient/build.gradle medicall-api-patient/
COPY medicall-api-hospital/build.gradle medicall-api-hospital/

RUN gradle dependencies --no-daemon --quiet || true

COPY . .

# Doctor API 빌드 스테이지
FROM base-builder AS doctor-builder
RUN gradle :medicall-api-doctor:bootJar --no-daemon --info

# Doctor API 실행 스테이지
FROM openjdk:17-jdk-slim AS doctor-runtime
WORKDIR /app

RUN addgroup --system medicall && adduser --system doctor --ingroup medicall

COPY --from=doctor-builder /home/gradle/project/medicall-api-doctor/build/libs/*.jar doctor-app.jar

RUN chown doctor:medicall doctor-app.jar

USER doctor
EXPOSE 8081

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "doctor-app.jar"]


# Patient API 빌드 스테이지
FROM base-builder AS patient-builder
RUN gradle :medicall-api-patient:bootJar --no-daemon --info

# Patient API 실행 스테이지
FROM openjdk:17-jdk-slim AS patient-runtime
WORKDIR /app

RUN addgroup --system medicall && adduser --system patient --ingroup medicall

COPY --from=patient-builder /home/gradle/project/medicall-api-patient/build/libs/*.jar patient-app.jar
RUN chown patient:medicall patient-app.jar

USER patient
EXPOSE 8082

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "patient-app.jar"]


# Hospital API 빌드 스테이지
FROM base-builder AS hospital-builder
RUN gradle :medicall-api-hospital:bootJar --no-daemon --info

# Hospital API 실행 스테이지
FROM openjdk:17-jdk-slim AS hospital-runtime
WORKDIR /app

RUN addgroup --system medicall && adduser --system hospital --ingroup medicall

COPY --from=hospital-builder /home/gradle/project/medicall-api-hospital/build/libs/*.jar hospital-app.jar
RUN chown hospital:medicall hospital-app.jar

USER hospital
EXPOSE 8083

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "hospital-app.jar"]