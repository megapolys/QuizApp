--liquibase formatted sql
--changeset d.losev:1717325285

set
    search_path to quiz_app;

insert into usr(active, username, password)
values (true, 'admin', '$2a$14$Ue5JGs9/wxorrwLOpqh3z.MsU8REWgsetQ/JVzqdWsN7ASNvycmm2');


insert into usr_role(user_id, role_id)
values (1, 1),
       (1, 51);