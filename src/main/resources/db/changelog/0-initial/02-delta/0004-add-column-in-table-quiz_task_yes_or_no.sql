--liquibase formatted sql
--changeset d.losev:1737093521

set
    search_path to quiz_app;

alter table quiz_task_yes_or_no
    add column if not exists task_id bigint;