# Build from the repository root (the shared monitoring library is needed):
#   docker build -f draft-service/Dockerfile .

# Stage 1: build the jar with the full JDK and Maven - none of this ends up in the final image
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /build
COPY monitoring monitoring
RUN --mount=type=cache,target=/root/.m2,sharing=locked cd monitoring && ./mvnw -B -q install -DskipTests
COPY draft-service service
RUN --mount=type=cache,target=/root/.m2,sharing=locked cd service && ./mvnw -B -q package -DskipTests

# Stage 2: only a slim Java runtime and the jar
FROM eclipse-temurin:25-jre-alpine
COPY --from=build /build/service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
