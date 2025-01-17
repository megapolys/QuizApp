--liquibase formatted sql
--changeset d.losev:1737093521

set
    search_path to quiz_app;

alter table quiz_task_five_variant
    add column if not exists task_id bigint;