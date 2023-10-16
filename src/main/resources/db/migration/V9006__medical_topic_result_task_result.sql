alter table if exists medical_task_result add topic_result_id bigint;
alter table if exists medical_task_result add constraint fk_medical_topic_result_task_result foreign key (topic_result_id) references medical_topic_result;
create index medical_result_task_topic_id_index on medical_task_result (topic_result_id);

update medical_task_result
set topic_result_id = (select t.medical_topic_result_id from medical_topic_result_results as t where t.results_id = id)
where topic_result_id IS NULL;

drop table medical_topic_result_results;
