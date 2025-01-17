--liquibase formatted sql
--changeset d.losev:1737093523

set
    search_path to quiz_app;

update quiz_task_yes_or_no
set task_id = qt.id
from (select id, quiz_task_yes_or_no_id from quiz_task) as qt
where task_id is null
  and qt.quiz_task_yes_or_no_id = quiz_task_yes_or_no.id;

