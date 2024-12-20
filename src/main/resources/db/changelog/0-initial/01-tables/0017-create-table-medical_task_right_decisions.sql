--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create table medical_task_right_decisions
(
    medical_task_id    bigint not null references medical_task (id),
    right_decisions_id bigint not null references decision (id),
    primary key (medical_task_id, right_decisions_id)
);