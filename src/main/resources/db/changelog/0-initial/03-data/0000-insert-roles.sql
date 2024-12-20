--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

insert into role (name)
values ('ADMIN'),
       ('USER');