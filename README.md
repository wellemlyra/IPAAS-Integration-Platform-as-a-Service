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
│       └── postman/            # Collection + Environment Postman
├── src/test/java/com/ipaas/tasks/ # Testes automatizados
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

## 🧰 Recursos Utilizados

O projeto foi desenvolvido utilizando um conjunto moderno de tecnologias:

| Recurso / Tecnologia        | Versão        | Observações |
|-----------------------------|--------------|-------------|
| **Java**                   | 21 (LTS)     | Versão mínima requerida para build e execução |
| **Quarkus**                | 3.x          | Framework principal para microsserviços |
| **Maven**                  | 3.9+         | Gerenciador de dependências |
| **Hibernate ORM Panache**  | 3.x (incluso no Quarkus) | Simplificação de repositórios e persistência |
| **Flyway**                 | 9.x+         | Controle de versão do banco de dados |
| **JUnit 5**                | 5.x          | Testes unitários |
| **RestAssured**            | 5.x          | Testes de integração com endpoints REST |
| **Docker**                 | 24.x+        | Contêineres da aplicação e banco |
| **Docker Compose**         | 2.x+         | Orquestração de múltiplos contêineres |
| **Postman**                | 10+          | Validação manual de endpoints via coleção pronta |

---

## ⚙️ Pré-requisitos de Ambiente

Para executar o projeto de forma **plug and play**, você precisa ter instalado:

- **Java 21 (ou superior)**  
  Verifique com:
  ```bash
  java -version
  ```
- **Maven 3.9+**  
  Verifique com:
  ```bash
  mvn -version
  ```
- **Docker 24.x+** e **Docker Compose 2.x+**  
  Verifique com:
  ```bash
  docker -v
  docker compose version
  ```
- **Git** (para clonar o repositório):
  ```bash
  git --version
  ```
- **Postman** (opcional, para testes manuais com a coleção já incluída):
  - Importar a collection e o environment em `src/main/resources/postman/`

### 💡 Observações
- Não é necessário instalar banco de dados localmente.
- O **Docker Compose** já sobe automaticamente a aplicação **+ banco de dados** configurados com scripts do Flyway.
- Após clonar e rodar `docker compose up -d --build`, a aplicação estará disponível em:
  ```
  http://localhost:8080/api/tasks
  ```

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

## 📬 Testando com Postman

O projeto já inclui uma **coleção Postman completa** e um **arquivo de Environment** na pasta:

```
src/main/resources/postman/
```

### 📂 Conteúdo:
- **ipaas_tasks_collection.json** → coleção com todos os endpoints (Users, Tasks e Subtasks).
- **ipaas_tasks_environment.json** → variáveis de ambiente prontas, incluindo `baseUrl`, `userId`, `taskId` e `subtaskId`.

Isso garante que você consiga testar a API de forma **plug and play**.

---

<details>
<summary><strong>🌐 Como importar no Postman</strong></summary>

1. Abra o Postman.
2. Clique em **Import** → selecione os arquivos da pasta `src/main/resources/postman/`.
3. Certifique-se de selecionar também o **Environment** para carregar variáveis automaticamente.
4. Execute as requisições em ordem:

  - **Users** → cria usuários e captura automaticamente `userId`, `ownerId` e `assigneeId`.
  - **Tasks** → cria tarefa vinculada, atualiza status e permite filtro por `status`.
  - **Subtasks** → cria subtarefas vinculadas e testa as regras de bloqueio de conclusão.
  - **Fluxo Rápido (E2E)** → executa tudo em sequência simulando o uso real da API.

</details>

---

### ✅ Benefícios
- Nenhuma configuração manual → basta importar e rodar.
- Variáveis de ambiente garantem reaproveitamento automático de IDs.
- Possibilita rodar cenários completos (CRUD + regras de negócio) em minutos.
- Documentação viva → a coleção serve como contrato de uso da API.

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

## 📖 Próximos passos

- Deploy em nuvem (Kubernetes/Openshift).
- Monitoramento com Prometheus/Grafana.
- Autenticação JWT para segurança.  
