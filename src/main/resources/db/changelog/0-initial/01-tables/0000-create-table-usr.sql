--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence usr_id_seq increment by 50;

create table usr
(
    id                   bigint  not null primary key default nextval('usr_id_seq'),
    activation_code      varchar,
    active               boolean not null,
    email                varchar,
    first_name           varchar,
    last_name            varchar,
    middle_name          varchar,
    password             varchar,
    username             varchar,
    repair_password_code varchar,
    birthday             date,
    male                 boolean
);

alter sequence usr_id_seq owned by usr.id;