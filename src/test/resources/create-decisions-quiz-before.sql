delete from quiz_task_decisions;
delete from quiz_task;
delete from quiz;
delete from decision;
delete from decision_group;

insert into decision_group(id, name) values
                                         (1, 'group1'),
                                         (2, 'group2'),
                                         (3, 'group3');
alter sequence decision_group_seq restart with 4;

insert into decision(id, name, description, group_id) values
                                                            (1, 'dec1', 'description1', null),
                                                            (2, 'dec1-2', 'description1', null),
                                                            (3, 'dec3', 'description3', null),

                                                            (4, 'dec4', 'description4', 1),
                                                            (5, 'dec5', 'description5', 1),

                                                            (6, 'dec6', 'description6', 2);
alter sequence decision_seq restart with 7;

insert into quiz(id, name, short_name) values (1, 'quiz', 'short_name');

insert into quiz_task(id, position, quiz_id) values
                                             (1, 1, 1),
                                             (2, 2, 1);

insert into quiz_task_decisions(quiz_task_id, decisions_id) values
                                                                (1, 1),
                                                                (1, 5),
                                                                (2, 5);

