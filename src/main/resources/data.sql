-- 1. Insertar Roles (Sintaxis MERGE para H2)
MERGE INTO roles KEY (id) VALUES (1, 'ROLE_USER');
MERGE INTO roles KEY (id) VALUES (2, 'ROLE_ADMIN');
