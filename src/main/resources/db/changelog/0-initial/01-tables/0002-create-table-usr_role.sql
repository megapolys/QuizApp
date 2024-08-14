--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create table usr_role
(
    user_id bigint not null references usr (id),
    role_id bigint not null references role (id),
    primary key (user_id, role_id)
);