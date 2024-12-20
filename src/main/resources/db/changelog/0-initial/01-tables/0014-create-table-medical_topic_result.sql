--liquibase formatted sql
--changeset d.losev:1717325285

set
search_path to quiz_app;

create sequence medical_topic_result_id_seq increment by 50;

create table medical_topic_result
(
    id               bigint not null primary key default nextval('medical_topic_result_id_seq'),
    medical_topic_id bigint not null references medical_topic (id),
    user_id          bigint not null references usr (id),
    complete_date    timestamp,
    last_update_date timestamp
);

alter sequence medical_topic_result_id_seq owned by medical_topic_result.id;