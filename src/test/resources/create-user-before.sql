create extension if not exists pgcrypto;
begin;
delete from user_role;
delete from usr;

insert into usr(id, active, username, password) values
                                                    (1, true, 'admin', crypt('adminPass', gen_salt('bf', 8))),
                                                    (2, true, 'kiril', crypt('kirilPass', gen_salt('bf', 8)));
insert into user_role(user_id, roles) values
                                          (1, 'USER'), (1, 'ADMIN'),
                                          (2, 'USER');
commit;