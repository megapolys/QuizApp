create extension if not exists pgcrypto;
insert into usr(id, username, password, active)
    values (1, 'admin', crypt('pass', gen_salt('bf', 8)), true);
insert into user_role(user_id, roles) values (1, 'USER'), (1, 'ADMIN');
