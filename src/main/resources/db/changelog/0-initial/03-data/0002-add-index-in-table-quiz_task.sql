--liquibase formatted sql
--changeset d.losev:1733559648

set
search_path to quiz_app;

create unique index if not exists quiz_task_quiz_id_position_unique_idx on quiz_task (quiz_id, position)