INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');

INSERT INTO users (username, password, name) VALUES ('admin@ob.com', '$2a$10$IvKJD.poRVCWu3GqJiNMNOz8RE7ob2WDU/r4DYWpuEXcGfgdzIXJu', 'Admin');
INSERT INTO users (username, password, name) VALUES ('joanoldaniel@gmail.com', '$2a$10$IvKJD.poRVCWu3GqJiNMNOz8RE7ob2WDU/r4DYWpuEXcGfgdzIXJu', 'Daniel');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);