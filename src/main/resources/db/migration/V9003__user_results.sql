alter table if exists medical_topic_result add user_id bigint;
alter table if exists medical_topic_result add constraint fk_usr_medical_results foreign key (user_id) references usr;

update quiz_result
set user_id = (select ur.usr_id from usr_results as ur where ur.results_id = id)
where user_id IS NULL;

update medical_topic_result
set user_id = (select ur.usr_id from usr_medical_results as ur where ur.medical_results_id = id)
where user_id IS NULL;

drop table usr_results;
drop table usr_medical_results;

create index medical_topic_result_user_id_index on medical_topic_result (user_id);
create index medical_topic_result_medical_topic_id_index on medical_topic_result (medical_topic_id);
create index quiz_result_quiz_id_index on quiz_result (quiz_id);
create index quiz_result_user_id_index on quiz_result (user_id);