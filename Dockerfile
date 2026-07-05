FROM eclipse-temurin:25-jdk AS build

WORKDIR /workspace
COPY . .
RUN --mount=type=cache,target=/root/.m2 ./mvnw clean package -DskipTests


FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
