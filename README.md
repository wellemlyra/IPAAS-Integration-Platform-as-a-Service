# iPaaS Tasks (Quarkus REST) – pronto pra uso

## Rodar
```bash
docker compose up --build
# API: http://localhost:8080
# Swagger: http://localhost:8080/q/swagger-ui
```


### Regras de banco (CHECK constraints)
- `tasks.status` e `subtasks.status` **restringidos** ao domínio: `NOVA | EM_ANDAMENTO | CONCLUIDA | CANCELADA`.
- `completed_at` **obrigatório** quando `status=CONCLUIDA` e **proibido** caso contrário (tarefas e subtarefas).
# IPAAS-Integration-Platform-as-a-Service
