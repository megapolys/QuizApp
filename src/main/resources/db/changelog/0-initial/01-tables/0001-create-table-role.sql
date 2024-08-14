--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence role_id_seq increment by 50;

create table role
(
    id   bigint not null primary key default nextval('role_id_seq'),
    name varchar
);

alter sequence role_id_seq owned by role.id;