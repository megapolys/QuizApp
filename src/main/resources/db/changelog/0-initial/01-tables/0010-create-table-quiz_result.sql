--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence quiz_result_id_seq increment by 50;

create table quiz_result
(
    id            bigint  not null primary key default nextval('quiz_result_id_seq'),
    quiz_id       bigint references quiz (id),
    user_id       bigint references usr (id),
    complete      boolean not null,
    complete_date timestamp
);

alter sequence quiz_result_id_seq owned by quiz_result.id;