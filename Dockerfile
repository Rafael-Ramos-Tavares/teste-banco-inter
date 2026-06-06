# Build stage
FROM maven:3.9.2-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Criar diretório para logs
RUN mkdir -p /app/logs

# Copiar JAR do stage anterior
COPY --from=builder /app/target/todo-list-api-*.jar app.jar

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD java -cp app.jar \
        -Dspring.boot.actuate.classpath.enabled=true \
        org.springframework.boot.loader.JarLauncher || exit 1

# Variáveis de ambiente padrão
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=prod

# Executar aplicação
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
