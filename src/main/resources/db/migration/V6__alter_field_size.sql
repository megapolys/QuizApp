alter table five_variant_task
alter column pre_question_text type varchar(1000) using pre_question_text::varchar(1000);
alter table yes_or_no_task
alter column pre_question_text type varchar(1000) using pre_question_text::varchar(1000);
alter table five_variant_task
alter column question_text type varchar(1000) using question_text::varchar(1000);
alter table yes_or_no_task
alter column question_text type varchar(1000) using question_text::varchar(1000);