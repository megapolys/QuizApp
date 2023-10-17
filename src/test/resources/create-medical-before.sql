delete from medical_task_result;
delete from medical_topic_result;
delete from medical_task_left_decisions;
delete from medical_task_right_decisions;
delete from decision;
delete from decision_group;
delete from medical_task;
delete from medical_topic;

insert into decision_group(id, name) values
                                         (1, 'group1')
;
insert into decision(id, name, description, group_id) values
                                                          (1, 'dec1', 'description1', 1),
                                                          (2, 'dec2', 'description2', 1),
                                                          (3, 'dec3', 'description3', null)
;
insert into medical_topic(id, name) values
                                        (1, 'topic 1'),
                                        (2, 'topic 2')
;
insert into medical_task(id, name, unit, topic_id) values
                                                 (1, 'task-1', 'ml/L', 1),
                                                 (2, 'task-2', null, 1),
                                                 (3, 'task-1', null, 2)
;
insert into medical_task_left_decisions(medical_task_id, left_decisions_id) values
                                                                                (1, 3),
                                                                                (2, 1)
;
insert into medical_task_right_decisions(medical_task_id, right_decisions_id) values
                                                                                 (3, 2);


