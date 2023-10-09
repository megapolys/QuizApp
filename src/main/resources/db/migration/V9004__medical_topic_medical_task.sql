alter table if exists medical_task add topic_id bigint;
alter table if exists medical_task add constraint fk_medical_topic_task foreign key (topic_id) references medical_topic;
create index medical_task_topic_id_index on medical_task (topic_id);

update medical_task
set topic_id = (select t.medical_topic_id from medical_topic_medical_tasks as t where t.medical_tasks_id = id)
where topic_id IS NULL;

drop table medical_topic_medical_tasks;