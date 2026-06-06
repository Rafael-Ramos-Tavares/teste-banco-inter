-- Script para criação do banco de dados e tabelas
-- Execute em SQL Server (SQL Server Management Studio ou outro cliente)

-- Criar banco de dados
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'todo_db')
BEGIN
    CREATE DATABASE todo_db;
END;
GO

-- Usar o banco de dados
USE todo_db;
GO

-- Criar tabela de tarefas
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'tasks')
BEGIN
    CREATE TABLE tasks (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        description TEXT NULL,
        status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
        created_at DATETIME2 NOT NULL DEFAULT GETUTCDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETUTCDATE()
    );
    
    -- Criar índices
    CREATE INDEX idx_status ON tasks(status);
    CREATE INDEX idx_created_at ON tasks(created_at);
    CREATE FULLTEXT CATALOG ftcat_tasks AS DEFAULT;
END;
GO

-- Inserir dados de exemplo (opcional)
INSERT INTO tasks (title, description, status, created_at, updated_at) VALUES
('Implementar API REST', 'Desenvolver endpoints para CRUD de tarefas', 'IN_PROGRESS', GETUTCDATE(), GETUTCDATE()),
('Documentar código', 'Adicionar comentários e documentação ao código', 'PENDING', GETUTCDATE(), GETUTCDATE()),
('Testes unitários', 'Escrever testes para as funcionalidades', 'PENDING', GETUTCDATE(), GETUTCDATE()),
('Deploy em produção', 'Configurar e fazer deploy da aplicação', 'PENDING', GETUTCDATE(), GETUTCDATE());
GO

-- Verificar dados inseridos
SELECT * FROM tasks;
GO
