--liquibase formatted sql
--changeset d.losev:1737093524

set
    search_path to quiz_app;

alter table quiz_task_yes_or_no
    add constraint quiz_task_yes_or_no_task_id_fk foreign key (task_id) references quiz_task (id);

alter table quiz_task_yes_or_no
    alter column task_id set not null;