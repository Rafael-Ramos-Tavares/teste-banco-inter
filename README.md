# Todo List API

Uma API RESTful para gerenciamento de tarefas (To-Do List) desenvolvida em Java com Spring Boot, seguindo princípios de Clean Code e Clean Architecture.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura do Projeto](#arquitetura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Instalação e Execução](#instalação-e-execução)
- [Endpoints da API](#endpoints-da-api)
- [Exemplos de Uso](#exemplos-de-uso)
- [Estrutura de Dados](#estrutura-de-dados)
- [Controle de Versão](#controle-de-versão)
- [Documentação Adicional](#documentação-adicional)

## 🎯 Visão Geral

Esta API permite que usuários:
- **Criar** novas tarefas
- **Listar** tarefas com paginação
- **Editar** tarefas existentes
- **Excluir** tarefas
- **Filtrar** tarefas por status
- **Buscar** tarefas por título

Cada tarefa possui:
- ID único
- Título (obrigatório)
- Descrição (opcional)
- Status (PENDENTE, EM ANDAMENTO, CONCLUÍDA)
- Data de criação
- Data de última atualização

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.1.0** - Framework web
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM (Object-Relational Mapping)
- **SQL Server 2019+** - Banco de dados
- **Maven** - Gerenciador de dependências e build
- **Lombok** - Redução de boilerplate
- **Spring Validation** - Validação de dados

### Ferramentas de Desenvolvimento
- **Git** - Controle de versão
- **Swagger/OpenAPI** - Documentação interativa da API
- **Postman** - Testes de API

## 🏗️ Arquitetura do Projeto

O projeto segue a **Clean Architecture** com separação em camadas:

```
projeto/
├── src/
│   ├── main/
│   │   ├── java/com/todolist/
│   │   │   ├── presentation/        # Camada de Apresentação (Controllers)
│   │   │   │   └── controller/
│   │   │   ├── application/         # Camada de Aplicação (DTOs, Mappers, Use Cases)
│   │   │   │   ├── dto/
│   │   │   │   ├── mapper/
│   │   │   │   └── usecase/
│   │   │   ├── domain/              # Camada de Domínio (Entidades, Repositórios)
│   │   │   │   ├── entity/
│   │   │   │   └── repository/
│   │   │   └── infrastructure/      # Camada de Infraestrutura (Config, Exceções)
│   │   │       ├── config/
│   │   │       └── exception/
│   │   └── resources/
│   │       ├── application.yml      # Configurações da aplicação
│   │       └── db/migration/        # Scripts SQL
│   └── test/                        # Testes automatizados
├── pom.xml                          # Dependências do Maven
├── README.md                        # Este arquivo
└── .gitignore                       # Arquivos ignorados pelo Git
```

### Responsabilidades de Cada Camada

1. **Presentation Layer** (Controllers)
   - Recebe requisições HTTP
   - Valida entrada de dados
   - Retorna respostas formatadas

2. **Application Layer** (Use Cases, DTOs, Mappers)
   - Implementa lógica de negócio
   - Converte entre DTOs e Entidades
   - Orquestra operações

3. **Domain Layer** (Entidades, Repositórios)
   - Define as entidades de negócio
   - Interfaces de persistência
   - Regras de domínio

4. **Infrastructure Layer** (Config, Exceções)
   - Configurações de framework
   - Tratamento de exceções
   - Integração com banco de dados

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17 ou superior**
  ```bash
  java -version
  ```
- **Maven 3.8.0 ou superior**
  ```bash
  mvn -version
  ```
- **SQL Server 2019 ou superior** (ou SQL Server Express)
  - [Download SQL Server Express](https://www.microsoft.com/pt-br/sql-server/sql-server-downloads)
- **Git** para controle de versão
  ```bash
  git --version
  ```

## 🔧 Configuração do Ambiente

### 1. Instalar e Configurar SQL Server

#### Windows (SQL Server Express)
1. Baixe [SQL Server Express](https://www.microsoft.com/pt-br/sql-server/sql-server-downloads)
2. Execute o instalador
3. Escolha "Basic" na instalação inicial
4. Anote a instância (ex: `SQLEXPRESS`)
5. Defina a autenticação como "Mixed Authentication"
6. Anote a senha do usuário `sa`

#### Verificar Conexão
```bash
sqlcmd -S localhost -U sa -P YourPassword@123
1> SELECT @@VERSION
2> GO
```

### 2. Criar Banco de Dados

Execute o script de inicialização:

```sql
-- Abra SQL Server Management Studio ou use sqlcmd
sqlcmd -S localhost -U sa -P YourPassword@123 -i "src/main/resources/db/migration/V1__init_database.sql"
```

Ou execute manualmente em SQL Server Management Studio:
```sql
CREATE DATABASE todo_db;
GO
-- Continue com o script do arquivo V1__init_database.sql
```

### 3. Configurar Credenciais do Banco

Edite `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=todo_db;encrypt=true;trustServerCertificate=true
    username: sa
    password: SUA_SENHA_AQUI  # Substitua pela sua senha do SQL Server
```

## 📦 Instalação e Execução

### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/todo-list-api.git
cd todo-list-api
```

### 2. Instalar Dependências

```bash
mvn clean install
```

### 3. Executar a Aplicação

```bash
mvn spring-boot:run
```

Ou via IDE:
- Abra o projeto em sua IDE preferida
- Navegue para `TodoListApplication.java`
- Clique com botão direito e selecione "Run As" > "Java Application"

A aplicação será iniciada em `http://localhost:8080/api`

### 4. Verificar Health da Aplicação

```bash
curl http://localhost:8080/api/health
```

## 🔌 Endpoints da API

### Criar Tarefa

**POST** `/api/tasks`

Request:
```json
{
  "title": "Implementar autenticação",
  "description": "Adicionar JWT à API",
  "status": "PENDING"
}
```

Response (201 Created):
```json
{
  "id": 1,
  "title": "Implementar autenticação",
  "description": "Adicionar JWT à API",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Obter Tarefa por ID

**GET** `/api/tasks/{id}`

Response (200 OK):
```json
{
  "id": 1,
  "title": "Implementar autenticação",
  "description": "Adicionar JWT à API",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Listar Todas as Tarefas

**GET** `/api/tasks?page=0&size=10&sort=createdAt,desc`

Response (200 OK):
```json
{
  "content": [
    {
      "id": 1,
      "title": "Tarefa 1",
      "description": "Descrição",
      "status": "PENDING",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

### Listar Tarefas por Status

**GET** `/api/tasks/status/{status}`

Parâmetros:
- `status` (PENDING, IN_PROGRESS, COMPLETED)

Response (200 OK):
```json
[
  {
    "id": 1,
    "title": "Tarefa Pendente",
    "description": "Descrição",
    "status": "PENDING",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

### Buscar Tarefas por Título

**GET** `/api/tasks/search?title=autenticação`

Response (200 OK):
```json
[
  {
    "id": 1,
    "title": "Implementar autenticação",
    "description": "Adicionar JWT à API",
    "status": "PENDING",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

### Atualizar Tarefa

**PUT** `/api/tasks/{id}`

Request:
```json
{
  "title": "Implementar autenticação JWT",
  "status": "IN_PROGRESS"
}
```

Response (200 OK):
```json
{
  "id": 1,
  "title": "Implementar autenticação JWT",
  "description": "Adicionar JWT à API",
  "status": "IN_PROGRESS",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:45:00"
}
```

### Deletar Tarefa

**DELETE** `/api/tasks/{id}`

Response (204 No Content)

## 📝 Exemplos de Uso

### Com cURL

```bash
# Criar tarefa
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Estudar Spring Boot",
    "description": "Aprender sobre Clean Architecture",
    "status": "PENDING"
  }'

# Listar todas as tarefas
curl http://localhost:8080/api/tasks

# Obter tarefa específica
curl http://localhost:8080/api/tasks/1

# Atualizar tarefa
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "IN_PROGRESS"
  }'

# Deletar tarefa
curl -X DELETE http://localhost:8080/api/tasks/1
```

### Com Postman

1. Abra Postman
2. Importe o arquivo de collection (ou crie manualmente)
3. Configure a variável de ambiente:
   - `base_url`: `http://localhost:8080/api`
4. Execute as requisições

### Com JavaScript/Fetch

```javascript
// Criar tarefa
fetch('http://localhost:8080/api/tasks', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    title: 'Nova tarefa',
    description: 'Descrição',
    status: 'PENDING'
  })
})
.then(response => response.json())
.then(data => console.log(data));

// Listar tarefas
fetch('http://localhost:8080/api/tasks')
  .then(response => response.json())
  .then(data => console.log(data));
```

## 📊 Estrutura de Dados

### Entidade Task

| Campo | Tipo | Descrição | Obrigatório |
|-------|------|-----------|------------|
| id | Long | Identificador único | Sim (Auto) |
| title | String | Título da tarefa | Sim |
| description | String | Descrição detalhada | Não |
| status | TaskStatus | PENDING, IN_PROGRESS, COMPLETED | Não (Default: PENDING) |
| createdAt | LocalDateTime | Data de criação | Sim (Auto) |
| updatedAt | LocalDateTime | Data da última atualização | Sim (Auto) |

### Enum TaskStatus

- **PENDING** (Pendente) - Tarefa ainda não iniciada
- **IN_PROGRESS** (Em Andamento) - Tarefa em execução
- **COMPLETED** (Concluída) - Tarefa finalizada

## 🌿 Controle de Versão (Git)

### Estratégia GitFlow

Este projeto utiliza **GitFlow** para gerenciar o versionamento:

```
main (produção)
  ├── release/ (preparação para lançamento)
  └── hotfix/ (correções urgentes)

develop (integração)
  ├── feature/ (novas funcionalidades)
  └── bugfix/ (correção de bugs)
```

### Comandos Básicos

```bash
# Inicializar Git
git init

# Adicionar arquivos
git add .

# Fazer commit com mensagem descritiva
git commit -m "feat: criar endpoints da API"

# Ver histórico
git log --oneline --graph --all

# Branches
git branch                    # Listar branches
git checkout -b develop       # Criar branch develop
git checkout main             # Mudar para main
git merge develop            # Mergear develop em main
```

### Convenção de Commits

Siga o padrão:
```
<type>(<scope>): <subject>

<body>

<footer>
```

Tipos:
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `style`: Formatação
- `refactor`: Refatoração
- `test`: Testes
- `chore`: Manutenção

Exemplo:
```bash
git commit -m "feat(task): adicionar endpoint de criação de tarefas"
git commit -m "fix(task): corrigir validação de título"
git commit -m "docs: atualizar README com exemplos de uso"
```

## 📚 Documentação Adicional

### Swagger/OpenAPI

A API possui documentação interativa disponível em:

```
http://localhost:8080/api/swagger-ui.html
```

Você pode:
- Visualizar todos os endpoints
- Testar as requisições diretamente
- Ver exemplos de request/response

### Logs da Aplicação

Os logs são salvo em `logs/` com o padrão:
```
[timestamp] - mensagem
```

Configure o nível de log em `application.yml`:
```yaml
logging:
  level:
    com.todolist: DEBUG
```

## 🧪 Testes

### Executar Testes

```bash
# Todos os testes
mvn test

# Teste específico
mvn test -Dtest=TaskControllerTest

# Com cobertura
mvn clean test jacoco:report
```

## 🚀 Deploy

### Build para Produção

```bash
# Criar JAR executável
mvn clean package

# Resultado: target/todo-list-api-1.0.0.jar
```

### Executar JAR

```bash
java -jar target/todo-list-api-1.0.0.jar
```

## 🔒 Boas Práticas Implementadas

✅ **Clean Code**
- Nomes descritivos
- Métodos pequenos e focados
- Comentários quando necessário

✅ **Clean Architecture**
- Separação em camadas
- Independência de frameworks
- Fácil manutenção

✅ **SOLID**
- Single Responsibility
- Open/Closed
- Liskov Substitution
- Interface Segregation
- Dependency Inversion

✅ **REST API**
- Verbos HTTP corretos
- Status codes apropriados
- Versionamento de API
- Paginação

✅ **Segurança**
- Validação de entrada
- Tratamento de exceções
- Encriptação de conexão SQL Server

## 📞 Suporte e Contribuições

Para reportar bugs ou sugerir melhorias:
1. Abra uma issue no repositório
2. Descreva o problema com detalhes
3. Se possível, adicione exemplos

## 📄 Licença

Este projeto está licenciado sob Apache License 2.0.

---

**Desenvolvido com ❤️ usando Java e Spring Boot**

Versão: 1.0.0  
Data: Junho 2026
