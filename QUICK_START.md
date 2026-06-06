# ⚡ Quick Start Guide - Como Começar em 5 Minutos

Um guia rápido para colocar a aplicação rodando em poucos minutos.

## 🚀 Opção 1: Com Docker Compose (Recomendado)

**Pré-requisitos**: Docker e Docker Compose

### Passo 1: Iniciar Serviços
```bash
cd c:\Users\Rafa\Desktop\projeto\teste banco inter
docker-compose up -d
```

### Passo 2: Aguardar Inicialização
```bash
# Verificar status
docker-compose ps

# Ver logs da API
docker-compose logs -f api
```

Quando ver "Tomcat started on port 8080", a API está pronta!

### Passo 3: Testar
```bash
# Criar uma tarefa
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Teste","description":"Minha primeira tarefa","status":"PENDING"}'

# Listar tarefas
curl http://localhost:8080/api/tasks

# Acessar Swagger
# Abra: http://localhost:8080/api/swagger-ui.html
```

### Passo 4: Parar (quando quiser)
```bash
docker-compose down
```

---

## 🔧 Opção 2: Executar Localmente (Maven)

**Pré-requisitos**: Java 17+, Maven 3.8+, SQL Server

### Passo 1: Preparar Banco de Dados

#### Windows - SQL Server Express
```bash
# Conectar ao SQL Server
sqlcmd -S localhost\SQLEXPRESS -U sa -P YourPassword@123

# Ou usar SQL Server Management Studio (SSMS)
# 1. Abra SSMS
# 2. Servidor: localhost\SQLEXPRESS
# 3. Login: sa / SuaSenha
```

#### Executar Script SQL
```bash
# Via linha de comando
sqlcmd -S localhost\SQLEXPRESS -U sa -P YourPassword@123 -i "src/main/resources/db/migration/V1__init_database.sql"

# Ou copie e execute em SSMS o arquivo:
# src/main/resources/db/migration/V1__init_database.sql
```

### Passo 2: Configurar Credenciais

Edite `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    username: sa
    password: SUA_SENHA_DO_SQL_SERVER  # ← Altere aqui
```

### Passo 3: Executar

```bash
# Via Maven
mvn spring-boot:run

# Ou abra em sua IDE:
# IntelliJ: Click em TodoListApplication.java → Run
# Eclipse: Right-click → Run As → Java Application
# VS Code: F5 (após instalar Extension Pack for Java)
```

### Passo 4: Testar

```bash
# API está em:
http://localhost:8080/api

# Swagger em:
http://localhost:8080/api/swagger-ui.html

# Teste com curl:
curl http://localhost:8080/api/tasks
```

---

## 📝 Testar a API

### Método 1: Swagger UI (Fácil)

1. Abra http://localhost:8080/api/swagger-ui.html
2. Expanda "Tasks"
3. Clique em "Try it out"
4. Preencha os dados
5. Clique em "Execute"

### Método 2: VS Code REST Client

Se estiver usando VS Code com REST Client extension:

1. Instale extensão REST Client
2. Abra arquivo `HTTP_REQUESTS.http`
3. Clique em "Send Request" acima de cada requisição

### Método 3: Postman

1. Abra [Postman](https://www.postman.com/)
2. Create New Request
3. Copie exemplos de [HTTP_REQUESTS.http](HTTP_REQUESTS.http)

### Método 4: cURL

```bash
# Criar tarefa
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Aprender Spring Boot",
    "description":"Estudar framework Spring Boot",
    "status":"IN_PROGRESS"
  }'

# Listar tarefas
curl http://localhost:8080/api/tasks

# Obter tarefa específica (ID 1)
curl http://localhost:8080/api/tasks/1

# Atualizar tarefa
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"status":"COMPLETED"}'

# Deletar tarefa
curl -X DELETE http://localhost:8080/api/tasks/1
```

---

## 📊 Exemplo de Resposta

### POST /api/tasks (Criar)

**Request:**
```json
{
  "title": "Implementar API",
  "description": "Criar REST API com Spring Boot",
  "status": "IN_PROGRESS"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "Implementar API",
  "description": "Criar REST API com Spring Boot",
  "status": "IN_PROGRESS",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### GET /api/tasks (Listar)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Implementar API",
      "description": "Criar REST API com Spring Boot",
      "status": "IN_PROGRESS",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

---

## 🔗 Links Rápidos

| Recurso | URL |
|---------|-----|
| **API Base** | http://localhost:8080/api |
| **Swagger/OpenAPI** | http://localhost:8080/api/swagger-ui.html |
| **Adminer (Banco)** | http://localhost:8081 |
| **API Docs JSON** | http://localhost:8080/api/v3/api-docs |

---

## ⚙️ Configurações Rápidas

### Mudar Porta
Edite `application.yml`:
```yaml
server:
  port: 9090  # Altere aqui
```

### Ativar Logs SQL
Edite `application.yml`:
```yaml
spring:
  jpa:
    show-sql: true  # Mostra SQL gerado
```

### Usar Perfil Dev (mais verbose)
```bash
# Maven
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Docker
docker-compose -f docker-compose.yml -e SPRING_PROFILES_ACTIVE=dev up
```

---

## 🐛 Troubleshooting Rápido

### Erro: "Connection refused"
**Solução**: SQL Server não está rodando
```bash
# Windows - Inicie o serviço
net start MSSQL$SQLEXPRESS

# Docker - Reinicie containers
docker-compose restart sqlserver
docker-compose restart api
```

### Erro: "Login failed for user 'sa'"
**Solução**: Senha incorreta
- Verifique senha em `application.yml`
- Ou mude em Docker: edite `docker-compose.yml`

### Erro: "Porta 8080 já em uso"
**Solução**: Mude a porta em `application.yml`
```yaml
server:
  port: 8081
```

### Erro: "Database 'todo_db' does not exist"
**Solução**: Execute o script SQL
```bash
# Docker - já está configurado
docker-compose up

# Local - execute o script manualmente
sqlcmd -S localhost -U sa -P YourPassword@123 -i "src/main/resources/db/migration/V1__init_database.sql"
```

---

## 📚 Documentação Completa

Se precisar de mais informações:

| Tópico | Arquivo |
|--------|---------|
| Documentação Geral | [README.md](README.md) |
| Arquitetura | [ARCHITECTURE.md](ARCHITECTURE.md) |
| Git/Versionamento | [GITFLOW.md](GITFLOW.md) |
| SQL Server Setup | [SQL_SERVER_SETUP.md](SQL_SERVER_SETUP.md) |
| Docker Setup | [DOCKER_SETUP.md](DOCKER_SETUP.md) |
| Exemplos HTTP | [HTTP_REQUESTS.http](HTTP_REQUESTS.http) |

---

## ✅ Checklist de Início

- [ ] Clone ou abra o projeto
- [ ] Escolha Opção 1 (Docker) ou Opção 2 (Local)
- [ ] Siga passos da opção escolhida
- [ ] Teste com Swagger, cURL ou Postman
- [ ] Veja resposta com sucesso (status 200/201)
- [ ] Explore outros endpoints
- [ ] Leia documentação completa se necessário

---

## 🎉 Parabéns!

Se chegou até aqui, a API está **funcionando e pronta para uso**!

**Próximos passos:**
1. Explorar os [HTTP_REQUESTS.http](HTTP_REQUESTS.http)
2. Ler [ARCHITECTURE.md](ARCHITECTURE.md) para entender o código
3. Implementar melhorias ou novas features
4. Fazer deploy em produção

---

**Última atualização**: Janeiro 2024  
**Versão**: 1.0.0
