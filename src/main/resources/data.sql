-- =========================================================
-- 1. ESTRUCTURA DE LA BASE DE DATOS (SCHEMA)
-- =========================================================

-- Usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(500) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    failed_login_attempts INT DEFAULT 0,
    last_password_change DATETIME NULL,
    password_expires_at DATETIME NULL,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    must_change_password BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB;

-- Clasificaciones
CREATE TABLE IF NOT EXISTS clasificacion_glucosa (
    id BIGINT PRIMARY KEY,
    nombre_rango VARCHAR(50) NOT NULL,
    valor_min INT NOT NULL,
    valor_max INT NOT NULL,
    descripcion VARCHAR(255)
) ENGINE=InnoDB;

-- Registros de Glucosa
CREATE TABLE IF NOT EXISTS registro_glucosa (
    id BIGINT PRIMARY KEY,
    valor INT NOT NULL,
    fecha DATETIME NOT NULL,
    clasificacion_id BIGINT,
    CONSTRAINT fk_registro_clasificacion
        FOREIGN KEY (clasificacion_id)
        REFERENCES clasificacion_glucosa(id)
        ON DELETE SET NULL
) ENGINE=InnoDB;


-- ==================================
-- 2. INSERCIÓN DE DATOS
-- ===================================

-- CLASIFICACIONES
INSERT IGNORE INTO clasificacion_glucosa (id, nombre_rango, valor_min, valor_max, descripcion) VALUES
(1, 'Hipoglucemia', 0, 70, 'Nivel de glucosa peligrosamente bajo.'),
(2, 'Normal', 71, 140, 'Nivel de glucosa en rango objetivo.'),
(3, 'Hiperglucemia', 141, 600, 'Nivel de glucosa alto.');

-- USUARIOS
INSERT IGNORE INTO users (id, username, password_hash, active, account_non_locked, last_password_change, password_expires_at, failed_login_attempts, email_verified, must_change_password) VALUES
(1, 'admin', '$2a$12$Pwc4eZNYg1FFw6w1N3EkCuDEZkNe7ox.BUi.yyDPfkW7iI6c9T/02', 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, 1, 0),
(2, 'jdoe', '$2a$12$Pwc4eZNYg1FFw6w1N3EkCuDEZkNe7ox.BUi.yyDPfkW7iI6c9T/02', 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 1, 0, 0),
(3, 'maria', '$2a$12$Pwc4eZNYg1FFw6w1N3EkCuDEZkNe7ox.BUi.yyDPfkW7iI6c9T/02', 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, 1, 1),
(4, 'blockeduser', '$2a$12$Pwc4eZNYg1FFw6w1N3EkCuDEZkNe7ox.BUi.yyDPfkW7iI6c9T/02', 0, 0, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 5, 0, 0);

-- REGISTROS DE GLUCOSA
INSERT IGNORE INTO registro_glucosa (id, valor, fecha, clasificacion_id) VALUES
(1, 65, '2026-04-27 08:00:00', 1),
(2, 110, '2026-04-27 14:00:00', 2),
(3, 190, '2026-04-27 20:00:00', 3);