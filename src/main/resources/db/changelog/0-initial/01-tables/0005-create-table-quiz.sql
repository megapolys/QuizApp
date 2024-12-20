--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence quiz_id_seq increment by 50;

create table quiz
(
    id         bigint  not null primary key default nextval('quiz_id_seq'),
    name       varchar not null,
    short_name varchar not null unique
);

alter sequence quiz_id_seq owned by quiz.id;