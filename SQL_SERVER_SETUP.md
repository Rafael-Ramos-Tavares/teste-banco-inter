# Guia de Configuração do SQL Server

Este documento descreve como configurar o SQL Server para o projeto Todo List API.

## 📥 Instalação do SQL Server

### Windows - SQL Server Express (Gratuito)

1. **Download:**
   - Acesse [Microsoft SQL Server Downloads](https://www.microsoft.com/pt-br/sql-server/sql-server-downloads)
   - Clique em "Download do SQL Server Express"

2. **Instalação:**
   - Execute o instalador `SQLServer2022-SSEI-Expr.exe`
   - Selecione "Instalação Básica"
   - Aceite os termos
   - Escolha o caminho de instalação
   - Aguarde a conclusão

3. **Configuração:**
   - Durante a instalação, configure:
     - Autenticação: **Mixed** (SQL Server e Windows)
     - Instância padrão ou nomeada (use o padrão: SQLEXPRESS)
     - Serviço: Iniciar automaticamente

4. **Verificar Instalação:**
   ```bash
   # Abra o Prompt de Comando como Administrador
   sqlcmd -S localhost\SQLEXPRESS -U sa -P YourPassword@123
   
   # Se conectar com sucesso, verá:
   1>
   
   # Digite para sair:
   EXIT
   ```

### Windows - SQL Server Management Studio (SSMS)

1. **Download:**
   - Acesse [SSMS Download](https://docs.microsoft.com/pt-br/sql/ssms/download-sql-server-management-studio-ssms)
   - Baixe a versão mais recente

2. **Instalação:**
   - Execute o instalador
   - Clique em "Install"
   - Aguarde a conclusão
   - Reinicie se solicitado

3. **Conexão:**
   - Abra SQL Server Management Studio
   - **Server name**: `localhost` ou `localhost\SQLEXPRESS`
   - **Authentication**: SQL Server Authentication
   - **Login**: `sa`
   - **Password**: A senha configurada durante a instalação
   - Clique em "Connect"

## 🗄️ Criar Banco de Dados

### Opção 1: SQL Server Management Studio (GUI)

1. Abra SQL Server Management Studio
2. Clique com botão direito em "Databases"
3. Selecione "New Database"
4. Digite o nome: `todo_db`
5. Clique em "OK"

### Opção 2: SQL Query (sqlcmd)

```bash
# Conectar ao SQL Server
sqlcmd -S localhost\SQLEXPRESS -U sa -P YourPassword@123

# Executar comando
1> CREATE DATABASE todo_db;
2> GO

# Verificar criação
3> SELECT name FROM sys.databases WHERE name = 'todo_db';
4> GO

# Sair
5> EXIT
```

### Opção 3: Executar Script SQL

```bash
# Executar arquivo SQL
sqlcmd -S localhost\SQLEXPRESS -U sa -P YourPassword@123 -i "src/main/resources/db/migration/V1__init_database.sql"
```

## 📊 Criar Tabelas

Execute o script SQL localizado em `src/main/resources/db/migration/V1__init_database.sql`:

```sql
USE todo_db;
GO

-- Criar tabela tasks
CREATE TABLE tasks (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at DATETIME2 NOT NULL DEFAULT GETUTCDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETUTCDATE()
);

-- Criar índices para melhor performance
CREATE INDEX idx_status ON tasks(status);
CREATE INDEX idx_created_at ON tasks(created_at);

-- Verificar tabelas criadas
SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'tasks';
```

## 🔌 Configurar Conexão na Aplicação

### arquivo: `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    # Para conexão com instância padrão
    url: jdbc:sqlserver://localhost:1433;databaseName=todo_db;encrypt=true;trustServerCertificate=true
    
    # Para conexão com SQL Server Express (nomeado)
    # url: jdbc:sqlserver://localhost\SQLEXPRESS:1433;databaseName=todo_db;encrypt=true;trustServerCertificate=true
    
    username: sa
    password: SUA_SENHA_AQUI  # Substituir pela sua senha
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
  
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.SQLServer2012Dialect
```

## 🔍 Verificar Conexão

### Com sqlcmd

```bash
sqlcmd -S localhost\SQLEXPRESS -U sa -P YourPassword@123 -d todo_db

-- Verificar tabelas
1> SELECT * FROM INFORMATION_SCHEMA.TABLES;
2> GO

-- Verificar dados
3> SELECT * FROM tasks;
4> GO

-- Sair
5> EXIT
```

### Com SQL Server Management Studio

1. Expandir "Databases"
2. Expandir "todo_db"
3. Expandir "Tables"
4. Verá "dbo.tasks"

### Com Spring Boot

Após iniciar a aplicação, verifique os logs:

```
...
Tomcat started on port 8080
...
HikariPool-1 - Connected to database
```

Se houver erro de conexão, verifique:
- Senha do SQL Server
- Porta (padrão: 1433)
- Nome do banco (todo_db)
- Firewall bloqueando a porta

## 🔐 Segurança

### Alterar Senha do SA

```sql
-- No SQL Server Management Studio
USE master;
GO

ALTER LOGIN sa WITH PASSWORD = 'NovaPassword@123';
GO
```

### Criar Usuário Específico

```sql
USE master;
GO

-- Criar login
CREATE LOGIN todoapp WITH PASSWORD = 'TodoApp@123!';
GO

-- Criar usuário no banco
USE todo_db;
GO

CREATE USER todoapp FOR LOGIN todoapp;
GO

-- Dar permissões
GRANT SELECT, INSERT, UPDATE, DELETE ON dbo.tasks TO todoapp;
GO

-- Usar nas configurações da aplicação
-- username: todoapp
-- password: TodoApp@123!
```

## 📈 Monitoramento

### Ver Conexões Ativas

```sql
SELECT * FROM sys.dm_exec_sessions WHERE database_id = DB_ID('todo_db');
```

### Ver Queries em Execução

```sql
SELECT * FROM sys.dm_exec_requests WHERE status = 'running';
```

### Ver Logs de Erro

```sql
SELECT * FROM sys.event_log WHERE severity >= 2 ORDER BY logged DESC;
```

## 🛠️ Troubleshooting

### Erro: "Login failed for user 'sa'"

**Solução:**
- Verifique a senha
- Verifique se o serviço SQL Server está rodando
- Verifique o modo de autenticação (Mixed)

### Erro: "Cannot open database 'todo_db'"

**Solução:**
- Crie o banco de dados
- Verifique o nome do banco (case-sensitive em SQL)

### Erro: "Connection timeout"

**Solução:**
- Verifique se SQL Server está rodando
- Verifique a porta (padrão: 1433)
- Verifique o firewall
- Verifique a URL de conexão

### Erro: "Encrypt not supported"

**Solução:**
- Adicione `encrypt=true;trustServerCertificate=true` na URL

### SQL Server não inicia

**Solução (Windows):**
```bash
# Abra Services (Services.msc)
# Procure por "SQL Server (SQLEXPRESS)"
# Clique em "Start"

# Ou via CMD:
net start MSSQL$SQLEXPRESS
```

## 📚 Recursos Úteis

- [Documentação Microsoft SQL Server](https://docs.microsoft.com/pt-br/sql/)
- [SQL Server JDBC Driver](https://docs.microsoft.com/pt-br/sql/connect/jdbc/microsoft-jdbc-driver-for-sql-server)
- [Spring Data JPA com SQL Server](https://spring.io/projects/spring-data-jpa)

---

**Para questões específicas do projeto, consulte o [README.md](README.md)**
