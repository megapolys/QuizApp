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

