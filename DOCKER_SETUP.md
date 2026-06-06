# Guia de Execução com Docker Compose

Este documento descreve como executar o projeto completo usando Docker Compose.

## 📋 Pré-requisitos

- **Docker** (versão 20.10+)
  - [Download Docker Desktop](https://www.docker.com/products/docker-desktop)
- **Docker Compose** (versão 2.0+)
  - Incluído no Docker Desktop

## 🚀 Execução Rápida

### 1. Iniciar os Serviços

```bash
# No diretório raiz do projeto
docker-compose up -d

# Ver status dos containers
docker-compose ps

# Ver logs da aplicação
docker-compose logs -f api

# Ver logs do SQL Server
docker-compose logs -f sqlserver
```

### 2. Acessar a Aplicação

- **API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **Adminer (DB)**: http://localhost:8081

### 3. Parar os Serviços

```bash
# Parar os containers
docker-compose down

# Parar e remover volumes
docker-compose down -v

# Parar apenas um serviço
docker-compose stop api
```

## 🔧 Operações Comuns

### Ver Logs Detalhados

```bash
# Logs em tempo real
docker-compose logs -f

# Apenas da API
docker-compose logs -f api

# Apenas do SQL Server
docker-compose logs -f sqlserver

# Últimas 100 linhas
docker-compose logs --tail=100
```

### Executar Comandos no Container

```bash
# Acessar shell do container API
docker-compose exec api /bin/sh

# Executar comando no SQL Server
docker-compose exec sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P YourPassword@123
```

### Reiniciar Serviços

```bash
# Reiniciar todos
docker-compose restart

# Reiniciar apenas a API
docker-compose restart api

# Reiniciar apenas o SQL Server
docker-compose restart sqlserver
```

### Reconstruir Imagens

```bash
# Reconstruir a imagem da API
docker-compose build api

# Reconstruir todas as imagens
docker-compose build

# Reconstruir e reiniciar
docker-compose up -d --build
```

## 📊 Acessar o Banco de Dados

### Com Adminer (Recomendado)

1. Acesse http://localhost:8081
2. Preencha:
   - **System**: Microsoft SQL Server
   - **Server**: `sqlserver`
   - **Username**: `sa`
   - **Password**: `YourPassword@123`
   - **Database**: `todo_db`
3. Clique em "Login"

### Com SQL Server Management Studio (SSMS)

1. Abra SQL Server Management Studio
2. Configure:
   - **Server name**: `localhost,1433`
   - **Authentication**: SQL Server Authentication
   - **Login**: `sa`
   - **Password**: `YourPassword@123`
3. Clique em "Connect"

### Com Command Line

```bash
# Acessar sqlcmd no container
docker-compose exec sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P YourPassword@123

# Ou executar diretamente
docker exec -it todo-db /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P YourPassword@123
```

## 🔐 Segurança e Configuração

### Alterar Credenciais do Banco

Edite `docker-compose.yml`:

```yaml
services:
  sqlserver:
    environment:
      SA_PASSWORD: "SuaNovaSenha@123"  # Altere aqui
  api:
    environment:
      DB_PASSWORD: "SuaNovaSenha@123"  # Altere aqui
```

Depois reinicie:
```bash
docker-compose down
docker-compose up -d
```

### Usar Variáveis de Ambiente

Crie arquivo `.env`:

```bash
# .env
SA_PASSWORD=YourPassword@123
DB_HOST=sqlserver
DB_PORT=1433
DB_NAME=todo_db
DB_USER=sa
JAVA_OPTS=-Xmx512m -Xms256m
SPRING_PROFILES_ACTIVE=prod
```

Use no `docker-compose.yml`:

```yaml
environment:
  SA_PASSWORD: ${SA_PASSWORD}
  DB_PASSWORD: ${SA_PASSWORD}
```

## 📦 Persistência de Dados

### Volumes

O SQL Server usa um volume nomeado `sqlserver_data`:

```bash
# Ver volumes
docker volume ls

# Inspecionar volume
docker volume inspect <volume_name>

# Remover volume
docker volume rm sqlserver_data
```

### Backup do Banco

```bash
# Executar backup
docker-compose exec sqlserver /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U sa -P YourPassword@123 \
  -Q "BACKUP DATABASE todo_db TO DISK = '/var/opt/mssql/backup/todo_db.bak'"

# Copiar backup do container
docker cp todo-db:/var/opt/mssql/backup/todo_db.bak ./
```

## 🐛 Troubleshooting

### Container não inicia

```bash
# Ver logs detalhados
docker-compose logs sqlserver
docker-compose logs api

# Remover container e reconstruir
docker-compose down
docker-compose up -d --build
```

### Erro de conexão com o banco

```bash
# Verificar se o container do DB está rodando
docker ps | grep sqlserver

# Verificar health
docker inspect todo-db --format='{{.State.Health.Status}}'

# Aguardar o DB inicializar (pode levar 30-40 segundos)
docker-compose up --wait
```

### Erro de porta em uso

Se a porta 1433 ou 8080 já estão em uso:

```yaml
# docker-compose.yml
services:
  sqlserver:
    ports:
      - "1434:1433"  # Altere de porta
  api:
    ports:
      - "8081:8080"  # Altere de porta
```

Depois atualize a URL de conexão:

```yaml
DB_HOST: sqlserver:1434
```

### Erro de memória insuficiente

Aumente a memória alocada para Docker:

1. Abra Docker Desktop
2. Vá em Settings > Resources
3. Aumente Memory (recomendado: pelo menos 4GB)
4. Aumente Swap

## 📊 Monitoramento

### Ver Estatísticas de Uso

```bash
# CPU, memória, rede
docker stats

# Apenas API
docker stats todo-api

# Apenas DB
docker stats todo-db
```

### Ver Eventos do Docker

```bash
# Monitorar eventos
docker events --filter 'container=todo-api'
```

## 🚀 Deploy em Produção

Para deploy em produção com Docker:

1. Altere `SPRING_PROFILES_ACTIVE` para `prod`
2. Configure variáveis de ambiente reais
3. Use senhas fortes
4. Configure um reverse proxy (nginx, traefik)
5. Use gerenciador de orquestração (Kubernetes, Docker Swarm)
6. Configure backups automáticos
7. Implemente monitoramento e logging centralizado

Exemplo com Swarm:
```bash
docker swarm init
docker stack deploy -c docker-compose.yml todo-app
```

## 📚 Recursos Úteis

- [Documentação Docker Compose](https://docs.docker.com/compose/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Microsoft SQL Server Docker Docs](https://hub.docker.com/_/microsoft-mssql-server)
