--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create table quiz_task_decisions
(
    quiz_task_id bigint not null references quiz_task (id),
    decisions_id bigint not null references decision (id),
    primary key (quiz_task_id, decisions_id)
);