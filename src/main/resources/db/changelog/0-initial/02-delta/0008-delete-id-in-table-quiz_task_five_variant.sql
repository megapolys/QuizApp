--liquibase formatted sql
--changeset d.losev:1737093526

set
    search_path to quiz_app;

alter table quiz_task_five_variant
    drop column id;

drop sequence if exists quiz_task_five_variant_id_seq;