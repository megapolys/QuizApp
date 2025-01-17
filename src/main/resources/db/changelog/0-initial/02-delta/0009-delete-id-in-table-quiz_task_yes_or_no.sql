--liquibase formatted sql
--changeset d.losev:1737093527

set
    search_path to quiz_app;

alter table quiz_task_yes_or_no
    drop column id;

drop sequence if exists quiz_task_yes_or_no_id_seq;