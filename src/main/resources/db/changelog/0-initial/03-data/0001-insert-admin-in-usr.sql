--liquibase formatted sql
--changeset d.losev:1717325285

set
    search_path to quiz_app;

insert into usr(active, username, password)
values (true, 'admin', '$2a$08$liW7gbEa.O1PCzTtyh9ekuxf2E7ZVRY0jUY/.oRg6CNbC6P4JUze6');


insert into usr_role(user_id, role_id)
values (1, 1),
       (1, 51);