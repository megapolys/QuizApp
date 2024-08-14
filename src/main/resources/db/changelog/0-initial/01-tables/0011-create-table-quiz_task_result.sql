--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence quiz_task_result_id_seq increment by 50;

create table quiz_task_result
(
    id        bigint  not null primary key default nextval('quiz_task_result_id_seq'),
    task_id   bigint  not null references quiz_task (id),
    quiz_id   bigint  not null references quiz_result (id),
    complete  boolean not null,
    variant   varchar,
    alt_score float4,
    text      varchar
);

alter sequence quiz_task_result_id_seq owned by quiz_task_result.id;