--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

insert into usr(active, username, password)
values (true, 'admin', '$2a$08$Ji1HTeI22XnMS4a7cZb50.n3SDcAaEYvJZ89tFqx7bygI9wdGFkly');


insert into usr_role(user_id, role_id)
values (1, 1),
       (1, 51);