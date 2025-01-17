--liquibase formatted sql
--changeset d.losev:1737093525

set
    search_path to quiz_app;

alter table quiz_task
    drop column quiz_task_five_variant_id;
alter table quiz_task
    drop column quiz_task_yes_or_no_id;