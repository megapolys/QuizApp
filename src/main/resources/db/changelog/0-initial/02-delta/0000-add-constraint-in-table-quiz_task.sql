--liquibase formatted sql
--changeset d.losev:1733559648

set
    search_path to quiz_app;

alter table quiz_task
    add constraint quiz_task_quiz_id_position_unique unique (quiz_id, position) deferrable initially immediate;