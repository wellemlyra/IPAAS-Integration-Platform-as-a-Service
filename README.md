# 🚀 iPaaS Tasks API

Uma plataforma **plug and play** para gestão de **Tarefas e Subtarefas**, construída sobre **Quarkus (Java 21)**, com banco relacional e scripts automáticos para criação de tabelas.  
Este projeto já vem pronto para uso com **Docker Compose** e inclui uma **coleção Postman completa** para validação dos endpoints.

---

## 📂 Estrutura do Projeto

<details>
<summary><strong>🌳 Estrutura de pastas</strong> (clique para expandir)</summary>

```
ipaas-tasks-quarkus/
├── docker-compose.yml          # Orquestração do ambiente (DB + API)
├── Dockerfile                  # Build da aplicação
├── pom.xml                     # Configuração Maven
├── src/
│   ├── main/java/com/ipaas/tasks/
│   │   ├── application/
│   │   │   ├── dto/            # Objetos de transferência de dados
│   │   │   ├── resource/       # Resources (endpoints REST)
│   │   │   └── mapper/         # MapStruct Mappers
│   │   ├── domain/
│   │   │   ├── model/          # Entidades de domínio
│   │   │   └── service/        # Regras de negócio (Services)
│   │   └── infrastructure/
│   │       └── repository/     # Persistência (Panache Repositories)
│   └── resources/
│       ├── application.yaml    # Configuração Quarkus
│       └── db/migration/       # Scripts Flyway (DDL + dados iniciais)
├── src/test/java/com/ipaas/tasks/ # Testes automatizados
└── postman/
    └── ipaas_tasks_collection.json  # Collection Postman completa
```

</details>

---

## 🎯 Por que essa estrutura?

- **Separação clara de camadas**:
    - `application` → camada de entrada, expõe APIs.
    - `domain` → regra de negócio isolada.
    - `infrastructure` → persistência e integrações.

- **Plug and Play**: basta rodar `docker compose up` → banco e aplicação sobem juntos.

- **Scripts automáticos**:
    - O diretório `db/migration/` contém **migrations Flyway**.
    - Ao iniciar, o container cria **tabelas, constraints e dados iniciais** automaticamente.

- **Coleção Postman inclusa**: valida todos os endpoints sem precisar escrever nada.

- **Benefício técnico**:
    - Fácil manutenção → cada camada com responsabilidade única.
    - Escalabilidade → isolamos services, entities e resources.
    - Portabilidade → sobe em qualquer ambiente com Docker.

---

## ⚡ Subindo o projeto (100% Plug and Play)

<details>
<summary><strong>Passo a passo</strong></summary>

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/IPAAS-Integration-Platform-as-a-Service.git
cd IPAAS-Integration-Platform-as-a-Service

# 2. Suba os containers
docker compose up -d --build

# 3. Verifique se os serviços estão rodando
docker ps

# 4. Acesse a API
http://localhost:8080/api/tasks

# 5. (Opcional) Veja os logs
docker logs -f ipaas-tasks-app
```

✅ Ao subir, o banco já terá todas as tabelas criadas automaticamente via scripts em `src/main/resources/db/migration/`.

</details>

---

## 📌 Resources (Endpoints REST)

<details>
<summary><strong>👤 UsersResource</strong></summary>

- **POST /api/users** → cria usuário (owner/assignee).
- **GET /api/users** → lista todos.
- **GET /api/users/{id}** → busca por ID.
- **PUT /api/users/{id}** → atualiza usuário.
- **DELETE /api/users/{id}** → remove usuário.

</details>

<details>
<summary><strong>📝 TasksResource</strong></summary>

- **POST /api/tasks** → cria tarefa.
- **GET /api/tasks** → lista todas ou filtra por `?status=NOVA|EM_ANDAMENTO|CONCLUIDA`.
- **GET /api/tasks/{id}** → busca tarefa por ID.
- **PUT /api/tasks/{id}** → atualiza tarefa completa.
- **PATCH /api/tasks/{id}/status** → atualiza apenas status (com regra de subtarefas concluídas).
- **DELETE /api/tasks/{id}** → remove tarefa.

</details>

<details>
<summary><strong>📌 SubtasksResource</strong></summary>

- **POST /api/tasks/{taskId}/subtasks** → cria subtarefa vinculada a uma tarefa.
- **GET /api/tasks/{taskId}/subtasks** → lista subtarefas da tarefa.
- **PATCH /api/subtasks/{id}/status** → atualiza status da subtarefa.
- **DELETE /api/subtasks/{id}** → remove subtarefa.

</details>

---

## 🛠️ Tecnologias utilizadas

- **Java 21 + Quarkus 3.x** (framework principal)
- **Hibernate ORM Panache** (persistência simplificada)
- **Flyway** (controle de versão de banco de dados)
- **Docker Compose** (orquestração)
- **Postman** (coleção pronta para testes)
- **JUnit 5 + RestAssured** (testes automatizados)

---

## 📬 Testando com Postman

Uma **coleção Postman completa** está incluída em `/postman/ipaas_tasks_collection.json`.

<details>
<summary><strong>Exemplos de uso</strong></summary>

- Criar usuário → já captura `userId` e `ownerId`.
- Criar tarefa → já preenche `taskId`.
- Criar subtarefa → já vincula ao `taskId`.
- Atualizar status → respeita regra de subtarefas.
- Fluxo E2E → executa todas as operações em sequência.

</details>

---

## 🧪 Testes Automatizados

O projeto conta com uma suíte de **testes automatizados** que garantem a qualidade e a estabilidade da aplicação:

- **Testes de unidade (JUnit 5)**  
  Validação das regras de negócio em `TaskService`, `SubtaskService` e `UserService`.  
  Exemplo: impedir conclusão de tarefa com subtarefas abertas.

- **Testes de integração (RestAssured + QuarkusTest)**  
  Executam chamadas HTTP reais contra os endpoints expostos, validando resposta, status code e payload.  
  Exemplo: criação de usuário → criação de tarefa → criação de subtarefa → atualização de status.

### ▶️ Como executar os testes

```bash
# Executar todos os testes
./mvnw test

# Executar testes de integração
./mvnw verify
```

Os testes sobem um **container de banco em memória (H2)**, garantindo isolamento e rapidez.

✅ Benefícios:
- Confiabilidade na evolução do sistema.
- Documentação viva (os testes mostram o fluxo esperado).
- Evitam regressões em deploys futuros.

---

## 🧩 Benefícios principais

- 🚀 **Subida rápida**: um único comando e tudo está rodando.
- 🔒 **Scripts automáticos**: garante consistência do schema.
- 🧪 **Cobertura de testes**: unitários e integração, com validação de regras críticas.
- 📬 **Postman ready-to-use**: sem perda de tempo criando requests.
- 🛠️ **Arquitetura limpa**: facilita manutenção e evolução.
- 🌍 **Portável**: sobe em qualquer máquina com Docker.

---

