--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence decision_group_id_seq increment by 50;

create table decision_group
(
    id   bigint  not null primary key default nextval('decision_group_id_seq'),
    name varchar not null unique
);

alter sequence decision_group_id_seq owned by decision_group.id;