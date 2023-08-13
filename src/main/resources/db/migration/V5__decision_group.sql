create table decision_group
(
    id   bigint not null,
    name varchar(255),
    primary key (id)
);
create table decision_group_decisions
(
    decision_group_id bigint not null,
    decisions_id      bigint not null,
    primary key (decision_group_id, decisions_id)
);
alter table if exists quiz_decision add column description varchar (1000);
alter table if exists quiz_decision add column group_id bigint;
alter table if exists decision_group_decisions drop constraint if exists UK_acy2px3ivm4clw23u9oyrj5rf;
alter table if exists decision_group_decisions add constraint UK_acy2px3ivm4clw23u9oyrj5rf unique (decisions_id);
create sequence decision_group_seq start with 1 increment by 50;
alter table if exists decision_group_decisions add constraint FK1e6kr9lqcdv4yy0wtxxso4jee foreign key (decisions_id) references quiz_decision;
alter table if exists decision_group_decisions add constraint FKb549hre1wcbtij5gxamf3ic9b foreign key (decision_group_id) references decision_group;
alter table if exists quiz_decision add constraint FKi0judm2es5qji5dtegrhip1o1 foreign key (group_id) references decision_group;