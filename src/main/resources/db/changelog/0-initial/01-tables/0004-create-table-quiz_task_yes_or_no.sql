--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence quiz_task_yes_or_no_id_seq increment by 50;

create table quiz_task_yes_or_no
(
    id                bigint not null primary key default nextval('quiz_task_yes_or_no_id_seq'),
    pre_question_text varchar,
    question_text     varchar,
    file_name         varchar,
    yes_weight        float4,
    no_weight         float4
);

alter sequence quiz_task_yes_or_no_id_seq owned by quiz_task_yes_or_no.id;