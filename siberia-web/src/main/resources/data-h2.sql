INSERT INTO env (id, name, description, create_by, update_by) VALUES
(1, 'dev', 'develop', 'sys', 'sys'),
(2, 'sit', 'sit', 'sys', 'sys'),
(3, 'prod', 'production', 'sys', 'sys');

INSERT INTO app (id, project, module, app_type, create_by, update_by) VALUES
(1, 'togather', 'togather-edge', 'SPRING_CLOUD', 'sys', 'sys'),
(2, 'togather', 'togather-news', 'SPRING_CLOUD', 'sys', 'sys'),
(3, 'togather', 'togather-todo', 'SPRING_CLOUD', 'sys', 'sys'),
(4, 'togather', 'togather-task', 'SPRING_CLOUD', 'sys', 'sys');

INSERT INTO app_config (id, app_id, env_id, create_by, update_by, content) VALUES
(1, 1, 1, 'sys', 'sys', '{"abc": "abc"}'),
(2, 1, 2, 'sys', 'sys', '{"abc": "abc"}'),
(3, 1, 3, 'sys', 'sys', '{"abc": "abc"}'),
(4, 2, 1, 'sys', 'sys', '{"abc": "abc"}'),
(5, 2, 2, 'sys', 'sys', '{"abc": "abc"}'),
(6, 2, 3, 'sys', 'sys', '{"abc": "abc"}');

INSERT INTO app_lock(id, app_id, env_id, lock_status, create_by, update_by) VALUES
(1, 1, 1, 'LOCKED', 'sys', 'sys'),
(2, 1, 2, 'UNLOCKED', 'sys', 'sys'),
(3, 1, 3, 'LOCKED', 'sys', 'sys'),
(4, 2, 1, 'LOCKED', 'sys', 'sys'),
(5, 2, 2, 'UNLOCKED', 'sys', 'sys'),
(6, 2, 3, 'LOCKED', 'sys', 'sys');

INSERT INTO pipeline(id, title, description, status, create_by, update_by) VALUES
(1, 'demo', 'pipeline for demo', 'RUNNING', 'sys', 'sys'),
(2, 'demo', 'pipeline for demo', 'RUNNING', 'sys', 'sys');

INSERT INTO pipeline_deployment(id, pipeline_id, build_no, app_id, create_by, update_by) VALUES
(1, 1, 1, 1, 'sys', 'sys');

INSERT INTO app_host(id, app_id, env_id, hosts, create_by, update_by) VALUES
(1, 1, 1, '["192.168.1.1", "192.168.1.2"]', 'sys', 'sys');
