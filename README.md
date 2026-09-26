# Lista de Tarefas API

API para gerenciamento de tarefas com hipermídias (HATEOAS), documentação (Swagger), validações personalizadas e tratamento de exceções.

![alt text](animacao.gif)

## 🔨 Tecnologias

- [SpringBoot](https://spring.io/projects/spring-boot)
- [SpringMVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Doc OpenAPI](https://springdoc.org/v2/#spring-webflux-support)
- [Spring HATEOAS](https://spring.io/projects/spring-hateoas)
- [PostgreSQL](https://www.postgresql.org/download/)

## ✨ Funcionalidades

- CRUD completo de tarefas
- HATEOAS com links dinâmicos
- Validações personalizadas
- Tratamento global de exceções
- Documentação interativa com Swagger

## 🧠 Práticas adotadas

- API REST
- Consultas com Spring Data JPA
- Injeção de Dependências
- Tratamento de Respostas de erro
- Geração automática do Swagger com a OpenAPI

## 🚀 Como Executar

### Pré-requisitos

- Java 26
- PostgreSQL 18
- (Opcional) Git

### 1. Clonar o repositório

```bash
git clone https://github.com/devVitoroliveira/ListaDeTarefas.git
cd ListaDeTarefas
```

### 2. Configurar o banco de dados

Crie o banco 'TodoList' no PostgreSQL

```sql
CREATE DATABASE "TodoList";
```

Configure as credenciais em src/main/resources/application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/TodoList
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

Nota: as tabelas são criadas automaticamente pelo Hibernate (ddl-auto=update).

### 3. Executar a aplicação

```bash
./mvnw spring-boot:run
```

### 4. Acessar a aplicação

- Swagger UI: http://localhost:8080/swagger-ui.html

## 🧪 Testes

O projeto conta com testes unitários para as camadas de **Service** e **Controller**, garantindo a qualidade da lógica de negócio e dos endpoints da API.

### Como rodar

```bash
./mvnw test
```

### O que é testado

**Service (TarefaServiceTest)**

- Cenários de sucesso (salvar, buscar, atualizar, deletar).
- Cenários de recurso não encontrado.
- Cenários de exceção (ex: DataIntegrityViolationException).
- Uso de Mockito para isolar o repositório.

**Controller (TarefaControllerTest)**

- Status HTTP corretos (200, 201, 400, 404, 405).
- Corpo da resposta (campos esperados, links HATEOAS).
- Uso de @WebMvcTest(TarefaController.class) e MockMvc para simular requisições HTTP.

### Ferramentas

- JUnit 5
- Mockito
- AssertJ
- Spring MockMvc

## 📖 Documentação da API

A documentação interativa da API está disponível via Swagger UI após iniciar a aplicação:

- **Local:** http://localhost:8080/swagger-ui.html

A documentação inclui:

- Descrição de todos os endpoints.
- Exemplos de requisição e resposta.
- Schemas dos DTOs com validações.
- Links HATEOAS de cada recurso.
