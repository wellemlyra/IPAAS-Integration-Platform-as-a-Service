-- Enforce status domain and completed_at rules

-- Normalize data to satisfy constraints
UPDATE tasks    SET completed_at = NULL WHERE status <> 'CONCLUIDA' AND completed_at IS NOT NULL;
UPDATE tasks    SET completed_at = now() WHERE status = 'CONCLUIDA'   AND completed_at IS NULL;
UPDATE subtasks SET completed_at = NULL WHERE status <> 'CONCLUIDA' AND completed_at IS NOT NULL;
UPDATE subtasks SET completed_at = now() WHERE status = 'CONCLUIDA'   AND completed_at IS NULL;

-- Allowed status values
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.table_constraints
    WHERE table_name='tasks' AND constraint_name='chk_tasks_status_allowed'
  ) THEN
    ALTER TABLE tasks
      ADD CONSTRAINT chk_tasks_status_allowed
      CHECK (status IN ('NOVA', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA'));
  END IF;
END$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.table_constraints
    WHERE table_name='subtasks' AND constraint_name='chk_subtasks_status_allowed'
  ) THEN
    ALTER TABLE subtasks
      ADD CONSTRAINT chk_subtasks_status_allowed
      CHECK (status IN ('NOVA', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA'));
  END IF;
END$$;

-- completed_at coherence
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.table_constraints
    WHERE table_name='tasks' AND constraint_name='chk_tasks_completed_at_rules'
  ) THEN
    ALTER TABLE tasks
      ADD CONSTRAINT chk_tasks_completed_at_rules
      CHECK (
        (status = 'CONCLUIDA' AND completed_at IS NOT NULL)
        OR
        (status <> 'CONCLUIDA' AND completed_at IS NULL)
      );
  END IF;
END$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.table_constraints
    WHERE table_name='subtasks' AND constraint_name='chk_subtasks_completed_at_rules'
  ) THEN
    ALTER TABLE subtasks
      ADD CONSTRAINT chk_subtasks_completed_at_rules
      CHECK (
        (status = 'CONCLUIDA' AND completed_at IS NOT NULL)
        OR
        (status <> 'CONCLUIDA' AND completed_at IS NULL)
      );
  END IF;
END$$;
