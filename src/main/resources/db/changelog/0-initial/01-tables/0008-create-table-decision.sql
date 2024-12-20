--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence decision_id_seq increment by 50;

create table decision
(
    id          bigint  not null primary key default nextval('decision_id_seq'),
    name        varchar not null unique,
    description varchar,
    group_id    bigint references decision_group (id)
);

alter sequence decision_id_seq owned by decision.id;