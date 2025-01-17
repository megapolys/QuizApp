--liquibase formatted sql
--changeset d.losev:1737093524

set
    search_path to quiz_app;

alter table quiz_task_five_variant
    add constraint quiz_task_five_variant_task_id_fk foreign key (task_id) references quiz_task (id);

alter table quiz_task_five_variant
    alter column task_id set not null;