alter table quiz_decision
alter column description type varchar(2000) using description::varchar(2000);