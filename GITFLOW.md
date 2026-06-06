# GitFlow - Guia de Versionamento

Este projeto utiliza a estratégia **GitFlow** para gerenciar o desenvolvimento e versionamento de código.

## 📊 Estrutura de Branches

```
main (produção)
  │
  ├── release/1.0.0 (preparação de releases)
  │
  └── develop (integração contínua)
       │
       ├── feature/task-crud (novas funcionalidades)
       ├── feature/authentication (novas funcionalidades)
       ├── bugfix/validation-error (correção de bugs)
       └── hotfix/critical-fix (correções urgentes em produção)
```

## 🚀 Fluxo de Desenvolvimento

### 1. Criar uma Nova Funcionalidade

```bash
# 1. Garantir que develop está atualizado
git checkout develop
git pull origin develop

# 2. Criar branch de feature
git checkout -b feature/descriptive-feature-name

# 3. Desenvolver a funcionalidade
# (fazer commits regularmente)
git commit -m "feat(scope): descrição da alteração"

# 4. Fazer push da branch
git push origin feature/descriptive-feature-name

# 5. Criar Pull Request (PR) no GitHub
# - Título: "Feature: Descrição da funcionalidade"
# - Description: Explicar o que foi feito e por quê
# - Assign: atribuir para revisão

# 6. Após aprovação, fazer merge em develop
git checkout develop
git pull origin develop
git merge --no-ff feature/descriptive-feature-name
git push origin develop

# 7. Deletar branch local e remoto
git branch -d feature/descriptive-feature-name
git push origin --delete feature/descriptive-feature-name
```

### 2. Corrigir um Bug

```bash
# Segue o mesmo fluxo das features, mas em bugfix branch
git checkout develop
git checkout -b bugfix/bug-description
# ... fazer correção ...
git commit -m "fix(scope): descrição da correção"
git push origin bugfix/bug-description
# Criar PR e fazer merge em develop
```

### 3. Preparar uma Release

```bash
# 1. Criar branch de release a partir de develop
git checkout develop
git pull origin develop
git checkout -b release/1.1.0

# 2. Ajustes finais, bumping version
# - Atualizar versão em pom.xml
git commit -m "chore: bump version to 1.1.0"

# 3. Push da release branch
git push origin release/1.1.0

# 4. Após testes, fazer merge em main e develop
git checkout main
git pull origin main
git merge --no-ff release/1.1.0
git tag -a v1.1.0 -m "Release version 1.1.0"
git push origin main
git push origin v1.1.0

git checkout develop
git pull origin develop
git merge --no-ff release/1.1.0
git push origin develop

# 5. Deletar branch de release
git branch -d release/1.1.0
git push origin --delete release/1.1.0
```

### 4. Hotfix (Correção Urgente em Produção)

```bash
# 1. Criar hotfix a partir de main
git checkout main
git pull origin main
git checkout -b hotfix/critical-bug-fix

# 2. Fazer correção
git commit -m "fix(critical): descrição da correção urgente"
git push origin hotfix/critical-bug-fix

# 3. Fazer merge em main e develop
git checkout main
git pull origin main
git merge --no-ff hotfix/critical-bug-fix
git tag -a v1.0.1 -m "Hotfix version 1.0.1"
git push origin main
git push origin v1.0.1

git checkout develop
git pull origin develop
git merge --no-ff hotfix/critical-bug-fix
git push origin develop

# 4. Deletar hotfix branch
git branch -d hotfix/critical-bug-fix
git push origin --delete hotfix/critical-bug-fix
```

## 📋 Convenção de Nomes para Branches

```
<type>/<scope-or-description>

Exemplos:
feature/add-task-validation
feature/implement-jwt-auth
bugfix/fix-task-update-error
hotfix/fix-database-connection
release/1.0.0
```

## 💬 Convenção de Mensagens de Commit

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Tipos de Commit

- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Mudanças em documentação
- `style`: Alterações de formatação, sem afetar lógica
- `refactor`: Refatoração de código, sem mudar funcionalidade
- `perf`: Melhorias de performance
- `test`: Adição ou atualização de testes
- `chore`: Mudanças em build, dependências, etc.
- `ci`: Alterações em CI/CD

### Exemplos

```bash
# Feature
git commit -m "feat(task): add endpoint to list tasks by status"

# Bug fix
git commit -m "fix(task): correct task update validation error"

# Documentation
git commit -m "docs(readme): add API endpoint examples"

# Refactoring
git commit -m "refactor(task): simplify task service logic"

# Com corpo explicativo
git commit -m "feat(auth): implement JWT authentication

- Add JWT token generation
- Add token validation middleware
- Add refresh token support

Closes #123"
```

## 🔍 Verificar o Estado dos Branches

```bash
# Ver branches locais
git branch

# Ver branches remotos
git branch -r

# Ver todos os branches
git branch -a

# Ver branches com último commit
git branch -v

# Deletar branch local
git branch -d nome-da-branch

# Forçar deleção
git branch -D nome-da-branch

# Deletar branch remoto
git push origin --delete nome-da-branch
```

## 📈 Ver Histórico de Commits

```bash
# Log simples
git log

# Log com uma linha por commit
git log --oneline

# Log com grafo visual
git log --graph --oneline --all

# Log de uma branch específica
git log develop --oneline

# Log entre duas branches
git log develop..feature/task-crud

# Log com estatísticas
git log --stat

# Log com diff
git log -p
```

## 🔄 Pull Requests (Code Review)

### Boas Práticas

1. **Antes de criar PR:**
   - Garantir que o código está funcionando
   - Executar testes locais
   - Verificar se o código segue as convenções

2. **Ao criar PR:**
   - Título descritivo e em inglês
   - Descrição explicativa das mudanças
   - Referência a issues relacionadas (ex: `Closes #123`)

3. **PR Template:**
   ```
   ## Descrição
   Breve descrição do que foi feito
   
   ## Tipo de Mudança
   - [ ] Feature nova
   - [ ] Bug fix
   - [ ] Breaking change
   - [ ] Documentação
   
   ## Checklist
   - [ ] Testes escritos e passando
   - [ ] Documentação atualizada
   - [ ] Sem warnings do compilador
   - [ ] Segue o coding style do projeto
   
   ## Issues Relacionadas
   Closes #123
   ```

4. **Após revisão:**
   - Fazer merge via `--no-ff` para manter histórico
   - Deletar a branch

## ⚙️ Configuração Git

```bash
# Configurar nome e email
git config user.name "Seu Nome"
git config user.email "seu.email@example.com"

# Configurar editor padrão
git config core.editor "code"

# Ver configurações
git config --list

# Configurações globais
git config --global user.name "Seu Nome"
git config --global user.email "seu.email@example.com"
```

## 🛠️ Troubleshooting Comum

### Remover arquivo já commitado
```bash
git rm --cached arquivo.txt
echo "arquivo.txt" >> .gitignore
git add .gitignore
git commit -m "chore: remove arquivo tracked"
```

### Desfazer último commit (sem perder mudanças)
```bash
git reset --soft HEAD~1
```

### Desfazer última mudança de um arquivo
```bash
git checkout -- arquivo.txt
```

### Rebase interativo para limpar commits
```bash
git rebase -i HEAD~3  # Últimos 3 commits
```

### Merge com squash (combinar commits)
```bash
git merge --squash feature/minha-feature
git commit -m "feat: minha funcionalidade completa"
```

---

Para mais informações, consulte a [documentação oficial do Git](https://git-scm.com/doc)
