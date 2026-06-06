# Arquitetura - Clean Architecture

Este documento descreve a arquitetura utilizada no projeto **Todo List API**, baseada nos princípios de **Clean Architecture** (também conhecida como Hexagonal Architecture ou Ports & Adapters).

## 🎯 Princípios Fundamentais

A Clean Architecture baseia-se em alguns princípios essenciais:

1. **Independência de Frameworks**: A arquitetura não deve depender de frameworks
2. **Testabilidade**: O sistema deve ser facilmente testável
3. **Independência de Interface de Usuário**: A UI pode ser alterada sem afetar a lógica
4. **Independência de Banco de Dados**: Trocar de BD não afeta a lógica
5. **Independência de Agências Externas**: Fácil adicionar ou remover integrações

## 📐 Estrutura em Camadas

O projeto está organizado em 4 camadas concêntricas:

```
┌─────────────────────────────────────────────┐
│         Presentation (Controllers)          │
│  ┌─────────────────────────────────────────┐│
│  │    Application (Use Cases, DTOs)        ││
│  │  ┌─────────────────────────────────────┐││
│  │  │  Domain (Entities, Interfaces)      │││
│  │  │  ┌─────────────────────────────────┐│││
│  │  │  │  Infrastructure (Config, External)│
│  │  │  └─────────────────────────────────┘│││
│  │  └─────────────────────────────────────┘││
│  └─────────────────────────────────────────┘│
└─────────────────────────────────────────────┘
```

## 🏗️ Descrição das Camadas

### 1. Presentation Layer (Camada de Apresentação)

**Localização**: `src/main/java/com/todolist/presentation/`

**Responsabilidades**:
- Receber requisições HTTP
- Validar entrada de dados básica
- Formatear respostas HTTP
- Lidar com erros e exceções

**Componentes**:
- **Controllers**: `TaskController.java`
  - Define endpoints REST
  - Mapeia URLs para operações
  - Valida requisições
  - Retorna respostas formatadas

**Exemplo**:
```java
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskRequestDTO request) {
        TaskResponseDTO response = taskUseCase.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

### 2. Application Layer (Camada de Aplicação)

**Localização**: `src/main/java/com/todolist/application/`

**Responsabilidades**:
- Implementar casos de uso (Use Cases)
- Converter entre DTOs e Entidades
- Orquestrar a lógica de negócio
- Coordenar transações

**Componentes**:

#### a) Use Cases (Casos de Uso)
- **Interface**: `TaskUseCase.java`
  - Define contratos para operações
  - Independente de implementação

- **Implementação**: `TaskUseCaseImpl.java`
  - Implementa a lógica de negócio
  - Valida regras de domínio
  - Coordena com repositórios

```java
@Service
@Transactional
public class TaskUseCaseImpl implements TaskUseCase {
    public TaskResponseDTO createTask(CreateTaskRequestDTO request) {
        Task task = Task.builder()
            .title(request.getTitle())
            .status(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING)
            .build();
        
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }
}
```

#### b) DTOs (Data Transfer Objects)
- **CreateTaskRequestDTO.java**: DTO para criar tarefas
- **UpdateTaskRequestDTO.java**: DTO para atualizar tarefas
- **TaskResponseDTO.java**: DTO para responder tarefas

Exemplo:
```java
@Data
public class CreateTaskRequestDTO {
    @NotBlank(message = "Título é obrigatório")
    private String title;
    
    private String description;
    private TaskStatus status;
}
```

#### c) Mappers
- **TaskMapper.java**: Converte entre Task e TaskResponseDTO

```java
@Component
public class TaskMapper {
    public TaskResponseDTO toDTO(Task task) {
        return TaskResponseDTO.builder()
            .id(task.getId())
            .title(task.getTitle())
            // ... outros campos
            .build();
    }
}
```

### 3. Domain Layer (Camada de Domínio)

**Localização**: `src/main/java/com/todolist/domain/`

**Responsabilidades**:
- Definir entidades de negócio
- Encapsular regras de domínio
- Definir interfaces de persistência
- Ser independente de frameworks

**Componentes**:

#### a) Entities (Entidades)
- **Task.java**: Representa uma tarefa no domínio
  - Encapsula dados e regras de negócio
  - Possui ciclo de vida (criação, atualização)

```java
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    
    @PrePersist
    protected void onCreate() {
        status = TaskStatus.PENDING;
    }
}
```

#### b) Value Objects
- **TaskStatus.java**: Enum que representa o status de uma tarefa

```java
public enum TaskStatus {
    PENDING("Pendente"),
    IN_PROGRESS("Em Andamento"),
    COMPLETED("Concluída");
}
```

#### c) Repository Interfaces
- **TaskRepository.java**: Interface que define operações de persistência
  - Abstrai a implementação do banco de dados
  - Permite trocar de BD sem afetar a lógica

```java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByTitleContainingIgnoreCase(String title);
}
```

### 4. Infrastructure Layer (Camada de Infraestrutura)

**Localização**: `src/main/java/com/todolist/infrastructure/`

**Responsabilidades**:
- Configurações de framework
- Tratamento de exceções
- Integração com sistemas externos
- Implementação de persistência

**Componentes**:

#### a) Exception Handling
- **ResourceNotFoundException.java**: Exceção customizada
- **GlobalExceptionHandler.java**: Tratador global de exceções

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            WebRequest request) {
        ErrorResponse error = ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

#### b) Configurações
- **OpenAPIConfiguration.java**: Configuração do Swagger/OpenAPI
- **application.yml**: Propriedades da aplicação

## 🔄 Fluxo de Dados

```
HTTP Request
    ↓
Controller (Presentation)
    ↓ (mapeia para DTO)
UseCase (Application)
    ↓ (mapeia para Entity)
Repository (Domain Interface)
    ↓
Database (Infrastructure)
    ↓ (retorna Entity)
Mapper (Application)
    ↓ (mapeia para DTO)
Controller (Presentation)
    ↓
HTTP Response
```

## 📦 Mapeamento de Arquivos

```
src/main/java/com/todolist/
├── presentation/           # Camada de Apresentação
│   └── controller/
│       └── TaskController.java
├── application/            # Camada de Aplicação
│   ├── dto/
│   │   ├── CreateTaskRequestDTO.java
│   │   ├── UpdateTaskRequestDTO.java
│   │   └── TaskResponseDTO.java
│   ├── mapper/
│   │   └── TaskMapper.java
│   └── usecase/
│       ├── TaskUseCase.java
│       └── impl/
│           └── TaskUseCaseImpl.java
├── domain/                 # Camada de Domínio
│   ├── entity/
│   │   ├── Task.java
│   │   └── TaskStatus.java
│   └── repository/
│       └── TaskRepository.java
└── infrastructure/         # Camada de Infraestrutura
    ├── config/
    │   └── OpenAPIConfiguration.java
    └── exception/
        ├── ResourceNotFoundException.java
        ├── GlobalExceptionHandler.java
        └── ErrorResponse.java
```

## 🧪 Benefícios da Clean Architecture

### 1. Independência de Teste
```java
// Fácil fazer mock do repository
@Mock
TaskRepository taskRepository;

@InjectMocks
TaskUseCaseImpl useCase;

@Test
void testCreateTask() {
    when(taskRepository.save(any())).thenReturn(task);
    useCase.createTask(request);
    verify(taskRepository).save(any());
}
```

### 2. Independência de Framework
Se trocar de Spring para outro framework:
- Domain Layer permanece igual
- Application Layer com pequenas mudanças
- Apenas Presentation muda

### 3. Facilidade de Manutenção
- Cada camada tem responsabilidade clara
- Fácil localizar código
- Fácil fazer mudanças

### 4. Reusabilidade
- Use Cases podem ser chamados por diferentes Controllers
- Domain Entities independentes de persistência
- Mappers podem converter para diferentes DTOs

## 🔗 Relacionamentos Entre Camadas

```
Presentation → Application
    ↓             ↓
  (HTTP)      (DTOs, Use Cases)
               ↓
            Domain
             ↓   ↓
          (Entity, Interface)
             ↓
        Infrastructure
             ↓
        (BD, Exceptions)
```

### Regras de Comunicação

1. **Apresentação → Aplicação**: ✅ Permitido
2. **Aplicação → Domínio**: ✅ Permitido
3. **Domínio → Infraestrutura**: ❌ Não permitido (uso interfaces)
4. **Infraestrutura → Apresentação**: ❌ Não permitido

## 📈 Escalabilidade

A arquitetura permite crescimento:

```
Nova Feature (ex: Autenticação)
├── Presentation
│   └── AuthController
├── Application
│   ├── usecase/AuthUseCase
│   └── dto/AuthDTO
├── Domain
│   ├── entity/User
│   └── repository/UserRepository
└── Infrastructure
    ├── config/SecurityConfig
    └── exception/UnauthorizedException
```

## 🎓 Recursos de Aprendizado

- [The Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Clean Code - Robert C. Martin](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)
- [Patterns of Enterprise Application Architecture](https://martinfowler.com/books/eaa.html)

---

**Implementação da arquitetura conforme padrão Clean Architecture e SOLID**
