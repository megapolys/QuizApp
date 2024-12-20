create
extension if not exists pgcrypto;
begin;
delete
from usr_role;
delete
from role;
delete
from usr;

insert into usr(id, active, username, password)
values (1, true, 'admin', crypt('adminPass', gen_salt('bf', 8))),
       (2, true, 'kiril', crypt('kirilPass', gen_salt('bf', 8))),
       (3, false, 'petr', crypt('petrPass', gen_salt('bf', 8)));
insert into role(id, name)
values (1, 'USER'),
       (2, 'ADMIN');
insert into usr_role(user_id, role_id)
values (1, 1),
       (1, 2),
       (2, 1),
       (3, 1);
commit;