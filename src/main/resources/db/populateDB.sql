DELETE
FROM user_roles;
DELETE
FROM users;
DELETE
FROM meals;
ALTER SEQUENCE global_seq
  RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2020-02-22 08:00:00','завтрак', 1000, 100000),
       ('2020-02-22 14:00:00','обед', 1000, 100000),
       ('2020-02-22 20:00:00','ужин', 1000, 100000),
       ('2020-02-21 08:00:00','завтрак', 1000, 100000),
       ('2020-02-21 14:00:00','обед', 1000, 100000),
       ('2020-02-21 20:00:00','ужин', 1000, 100000),
       ('2020-02-22 12:00:00','ужин', 800, 100001);