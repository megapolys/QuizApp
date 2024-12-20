--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence medical_topic_id_seq increment by 50;

create table medical_topic
(
    id   bigint  not null primary key default nextval('medical_topic_id_seq'),
    name varchar not null unique
);

alter sequence medical_topic_id_seq owned by medical_topic.id;