--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence medical_task_id_seq increment by 50;

create table medical_task
(
    id          bigint not null primary key default nextval('medical_task_id_seq'),
    name        varchar,
    unit        varchar,
    topic_id    bigint not null references medical_topic (id),
    left_left   float4,
    left_mid    float4,
    right_mid   float4,
    right_right float4
);

alter sequence medical_task_id_seq owned by medical_task.id;