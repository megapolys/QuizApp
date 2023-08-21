alter table if exists quiz_result add column user_id bigint;
alter table if exists quiz_result add constraint FK5bvbosraxn0k3vk4s5v8u63bw foreign key (user_id) references usr;
drop table if exists usr_quizzes;