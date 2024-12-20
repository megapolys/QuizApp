--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence quiz_task_id_seq increment by 50;

create table quiz_task
(
    id                        bigint  not null primary key default nextval('quiz_task_id_seq'),
    quiz_id                   bigint  not null references quiz (id),
    position                  integer not null,
    quiz_task_five_variant_id bigint references quiz_task_five_variant (id),
    quiz_task_yes_or_no_id    bigint references quiz_task_yes_or_no (id)
);

alter sequence quiz_task_id_seq owned by quiz_task.id;