--liquibase formatted sql
--changeset d.losev:1736070222

set search_path to quiz_app;

alter table medical_task_result
    drop column if exists alt_score;