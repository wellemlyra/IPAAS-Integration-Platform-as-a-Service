-- Habilita extensão para gerar UUIDs
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ===============================
-- Criação de tabela de usuários
-- ===============================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(150) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT now()
);

-- ===============================
-- Criação de tabela de tarefas
-- ===============================
CREATE TABLE tasks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    assignee_id UUID,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_tasks_user FOREIGN KEY (assignee_id) REFERENCES users (id) ON DELETE SET NULL
);

-- ===============================
-- Usuário admin inicial
-- ===============================
INSERT INTO users (email, password, name, role)
VALUES ('admin@ipaas.local', 'admin', 'Administrador', 'ADMIN');
