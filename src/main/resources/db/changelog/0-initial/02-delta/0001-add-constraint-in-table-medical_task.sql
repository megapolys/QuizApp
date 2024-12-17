--liquibase formatted sql
--changeset d.losev:1734412815

set
    search_path to quiz_app;

alter table medical_task
    add constraint medical_task_topic_id_name_uniq_idx unique (topic_id, name);