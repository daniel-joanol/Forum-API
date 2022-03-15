INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');

INSERT INTO users (username, password, email, is_activated) VALUES ('admin', '$2a$10$IvKJD.poRVCWu3GqJiNMNOz8RE7ob2WDU/r4DYWpuEXcGfgdzIXJu', 'admin@ob.com', true);

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);