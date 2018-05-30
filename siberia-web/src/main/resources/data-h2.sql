INSERT INTO env (name, description, create_by, update_by) VALUES
('dev', 'develop', 'sys', 'sys'),
('sit', 'sit', 'sys', 'sys'),
('prod', 'production', 'sys', 'sys');

INSERT INTO app ( project, module, app_type, create_by, update_by ) VALUES
('togather', 'togather-edge', 'SPRING_CLOUD', 'sys', 'sys'),
('togather', 'togather-news', 'SPRING_CLOUD', 'sys', 'sys'),
('togather', 'togather-todo', 'SPRING_CLOUD', 'sys', 'sys'),
('togather', 'togather-task', 'SPRING_CLOUD', 'sys', 'sys');
