-- ===============================
-- Tarefas de exemplo para o admin
-- ===============================

-- Pegamos o id do admin criado no V1
WITH admin_user AS (
    SELECT id FROM users WHERE email = 'admin@ipaas.local' LIMIT 1
)
INSERT INTO tasks (title, description, status, assignee_id)
VALUES
('Configurar ambiente Quarkus',
 'Preparar projeto Quarkus com PostgreSQL e Flyway',
 'IN_PROGRESS',
 (SELECT id FROM admin_user)),

('Criar tela de login',
 'Implementar tela de login no Angular com autenticação simples',
 'PENDING',
 (SELECT id FROM admin_user)),

('Documentar API',
 'Gerar documentação da API com Swagger UI',
 'PENDING',
 (SELECT id FROM admin_user));
