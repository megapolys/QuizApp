--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence medical_task_result_id_seq increment by 50;

create table medical_task_result
(
    id              bigint not null primary key default nextval('medical_task_result_id_seq'),
    medical_task_id bigint not null references medical_task (id),
    topic_result_id bigint not null references medical_topic_result (id),
    value           float4,
    alt_score       float4
);

alter sequence medical_task_result_id_seq owned by medical_task_result.id;