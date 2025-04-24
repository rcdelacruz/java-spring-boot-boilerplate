-- Insert admin user
INSERT INTO users (username, email, password, first_name, last_name, enabled)
VALUES ('admin', 'admin@example.com', '$2a$10$yRxRc5XMKzozH.q55fZV.uujo6k4r/J5l.jQ.xXhhJ.zXnRkZ3BQi', 'Admin', 'User', true);

-- Insert roles for admin user
INSERT INTO user_roles (user_id, role)
VALUES (1, 'ADMIN'), (1, 'USER');

-- Note: The password is 'password' encoded with BCrypt