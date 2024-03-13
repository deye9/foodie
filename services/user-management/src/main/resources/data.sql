insert into roles(id, role_name, role_description, created_at)
values
('105fc2b7-c5b1-4519-bb98-e8916fe49e86', 'USER', 'Application User', now()),
('e4482990-c42a-409e-be49-b76a56bbca87', 'RIDER', 'Application Rider', now()),
('52f6604b-57a1-4151-9776-4a834f66b8e3', 'ADMIN', 'Application Admin', now()),
('4143b826-3db9-452c-90d5-bbb96e78276a', 'FOODIE', 'Application Foodie', now()),
('a8a02c79-a9d2-4c3a-8866-5cac04f24bda', 'MANAGER', 'Application Manager', now());

insert into permissions(id, permission_name, permission_description, can_create, can_read, can_update, can_delete, created_at)
values
('f0a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c', 'READ', 'Read Permission', false, true, false, false, now()),
('e4e5d0a2-2a5d-4e0e-9b6e-3a8d8f5b8e1a', 'CREATE', 'Create Permission', true, false, false, false, now()),
('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', 'DELETE', 'Delete Permission', false, false, false, true, now()),
('6d7e8f9a-b0c1-d2e3-f4a5-b6c7d8e9f0a1', 'UPDATE', 'Update Permission', false, false, true, false, now());

insert into role_permissions(id, role_id, permission_id, created_at)
values
('a4e5d0a2-2a5d-4e0e-9b6e-3a8d8f5b8e1a', '105fc2b7-c5b1-4519-bb98-e8916fe49e86', 'e4e5d0a2-2a5d-4e0e-9b6e-3a8d8f5b8e1a', now()),
('b0a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c', '105fc2b7-c5b1-4519-bb98-e8916fe49e86', 'f0a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c', now()),
('cd7e8f9a-b0c1-d2e3-f4a5-b6c7d8e9f0a1', '105fc2b7-c5b1-4519-bb98-e8916fe49e86', '6d7e8f9a-b0c1-d2e3-f4a5-b6c7d8e9f0a1', now()),
('db3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', '105fc2b7-c5b1-4519-bb98-e8916fe49e86', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', now()),

('ef9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c', '52f6604b-57a1-4151-9776-4a834f66b8e3', 'e4e5d0a2-2a5d-4e0e-9b6e-3a8d8f5b8e1a', now()),
('fd5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '52f6604b-57a1-4151-9776-4a834f66b8e3', 'f0a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c', now()),
('ab1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e', '52f6604b-57a1-4151-9776-4a834f66b8e3', '6d7e8f9a-b0c1-d2e3-f4a5-b6c7d8e9f0a1', now()),
('bf7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c', '52f6604b-57a1-4151-9776-4a834f66b8e3', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', now()),

('c4e5d0a2-2a5d-4e0e-9b6e-3a8d8f5b8e1a', 'a8a02c79-a9d2-4c3a-8866-5cac04f24bda', 'e4e5d0a2-2a5d-4e0e-9b6e-3a8d8f5b8e1a', now()),
('d0a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c', 'a8a02c79-a9d2-4c3a-8866-5cac04f24bda', 'f0a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c', now()),
('ed7e8f9a-b0c1-d2e3-f4a5-b6c7d8e9f0a1', 'a8a02c79-a9d2-4c3a-8866-5cac04f24bda', '6d7e8f9a-b0c1-d2e3-f4a5-b6c7d8e9f0a1', now()),
('fb3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', 'a8a02c79-a9d2-4c3a-8866-5cac04f24bda', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', now()),

('af9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c', '4143b826-3db9-452c-90d5-bbb96e78276a', 'e4e5d0a2-2a5d-4e0e-9b6e-3a8d8f5b8e1a', now()),
('bd5e6f7a-8b9c-0d1e-2f3a-4b5c6d7e8f9a', '4143b826-3db9-452c-90d5-bbb96e78276a', 'f0a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c', now()),
('cb1c2d3e-4f5a-6b7c-8d9e-0f1a2b3c4d5e', '4143b826-3db9-452c-90d5-bbb96e78276a', '6d7e8f9a-b0c1-d2e3-f4a5-b6c7d8e9f0a1', now()),
('df7a8b9c-0d1e-2f3a-4b5c-6d7e8f9a0b1c', '4143b826-3db9-452c-90d5-bbb96e78276a', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', now()),

('14e5d0a2-2a5d-4e0e-9b6e-3a8d8f5b8e1a', 'e4482990-c42a-409e-be49-b76a56bbca87', 'e4e5d0a2-2a5d-4e0e-9b6e-3a8d8f5b8e1a', now()),
('20a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c', 'e4482990-c42a-409e-be49-b76a56bbca87', 'f0a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c', now()),
('6d7e8f9a-b0c1-d2e3-f4a5-b6c7d8e9f0a1', 'e4482990-c42a-409e-be49-b76a56bbca87', '6d7e8f9a-b0c1-d2e3-f4a5-b6c7d8e9f0a1', now()),
('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', 'e4482990-c42a-409e-be49-b76a56bbca87', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', now());

insert into users(id, firstname, lastname, password, email, created_at)
values
('8f9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c', 'User', 'User', 'password', 'user@mail.com', now()),
('e4e5d0a2-2a5d-4e0e-9b6e-3a8d8f5b8e1a', 'Admin', 'Admin', 'password', 'admin@mail.com', now()),
('2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', 'Rider', 'Rider', 'password', 'rider@mail.com', now()),
('6d7e8f9a-b0c1-d2e3-f4a5-b6c7d8e9f0a1', 'Foodie', 'Foodie', 'password', 'foodie@mail.com', now()),
('f0a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c', 'Manager', 'Manager', 'password', 'manager@mail.com', now());

insert into user_roles(id, user_id, role_id, created_at)
values
('75495756-0410-4192-ba24-32bd62e21579', '8f9a0b1c-2d3e-4f5a-6b7c-8d9e0f1a2b3c', '105fc2b7-c5b1-4519-bb98-e8916fe49e86', now()),
('20987afc-3a85-481f-8461-1b8a54d929b9', 'e4e5d0a2-2a5d-4e0e-9b6e-3a8d8f5b8e1a', '52f6604b-57a1-4151-9776-4a834f66b8e3', now()),
('e135a17c-e7cc-4775-b753-febe4d32f6dc', '2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e', 'a8a02c79-a9d2-4c3a-8866-5cac04f24bda', now()),
('ace83408-7806-4c85-9005-dc674a6a8b40', '6d7e8f9a-b0c1-d2e3-f4a5-b6c7d8e9f0a1', '4143b826-3db9-452c-90d5-bbb96e78276a', now()),
('d981cb4e-6745-40b5-be66-e874db0622d0', 'f0a1b2c3-d4e5-6f7a-8b9c-0d1e2f3a4b5c', 'e4482990-c42a-409e-be49-b76a56bbca87', now());