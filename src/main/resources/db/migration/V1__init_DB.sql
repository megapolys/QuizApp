create table five_variant_task
(
    id            bigint not null,
    fifth_weight  float4,
    file_name     varchar(255),
    first_weight  float4,
    fourth_weight float4,
    question_text varchar(255),
    second_weight float4,
    third_weight  float4,
    primary key (id)
);
create table five_variant_task_quizzes
(
    five_variant_task_id bigint not null,
    quizzes_id           bigint not null,
    primary key (five_variant_task_id, quizzes_id)
);
create table quiz
(
    id         bigint       not null,
    name       varchar(255) not null,
    short_name varchar(255) not null,
    primary key (id)
);
create table quiz_decision
(
    id   bigint not null,
    name varchar(255),
    primary key (id)
);
create table quiz_result
(
    id            bigint  not null,
    complete      boolean not null,
    complete_date timestamp(6),
    quiz_id       bigint,
    primary key (id)
);
create table quiz_task
(
    id                   bigint  not null,
    position             integer not null,
    five_variant_task_id bigint,
    quiz_id              bigint  not null,
    yes_or_no_task_id    bigint,
    primary key (id)
);
create table quiz_task_decisions
(
    quiz_task_id bigint not null,
    decisions_id bigint not null,
    primary key (quiz_task_id, decisions_id)
);
create table quiz_task_result
(
    id        bigint  not null,
    alt_score float4,
    complete  boolean not null,
    text      varchar(255),
    variant   varchar(255),
    quiz_id   bigint  not null,
    task_id   bigint,
    primary key (id)
);
create table user_role
(
    user_id bigint not null,
    roles   varchar(255)
);
create table usr
(
    id                   bigint  not null,
    activation_code      varchar(255),
    active               boolean not null,
    email                varchar(255),
    first_name           varchar(255),
    last_name            varchar(255),
    male                 boolean,
    password             varchar(255),
    repair_password_code varchar(255),
    username             varchar(255),
    year_born            integer,
    primary key (id)
);
create table usr_quizzes
(
    usr_id     bigint not null,
    quizzes_id bigint not null,
    primary key (usr_id, quizzes_id)
);
create table usr_results
(
    usr_id     bigint not null,
    results_id bigint not null,
    primary key (usr_id, results_id)
);
create table yes_or_no_task
(
    id            bigint not null,
    file_name     varchar(255),
    no_weight     float4,
    question_text varchar(255),
    yes_weight    float4,
    primary key (id)
);

alter table if exists five_variant_task_quizzes add constraint UK_94pwdheti9s5cu4klnys2at6f unique (quizzes_id);
alter table if exists quiz add constraint UK_imux4m190glnqxeh8gapkk2t unique (name);
alter table if exists quiz add constraint UK_lkub756ypceen0vr5hd7vkys5 unique (short_name);
alter table if exists usr add constraint UK_dfui7gxngrgwn9ewee3ogtgym unique (username);
alter table if exists usr_results add constraint UK_eifmkveerl9nd981sk8ye1jqx unique (results_id);
alter table if exists five_variant_task_quizzes add constraint FKk7q74il3has71atd98xilo9wt foreign key (quizzes_id) references quiz;
alter table if exists five_variant_task_quizzes add constraint FKlhm55io3bucftkhrmsabnxa0f foreign key (five_variant_task_id) references five_variant_task;
alter table if exists quiz_result add constraint FKd49de4d3rwgtndq0n51w1isbx foreign key (quiz_id) references quiz;
alter table if exists quiz_task add constraint FKh78yihj5bdam4ilm1du16vjks foreign key (five_variant_task_id) references five_variant_task;
alter table if exists quiz_task add constraint FK491bs5fyyby1i3e69l8qdke81 foreign key (quiz_id) references quiz;
alter table if exists quiz_task add constraint FKmku14kiutp58ceoeygdcjegc1 foreign key (yes_or_no_task_id) references yes_or_no_task;
alter table if exists quiz_task_decisions add constraint FK6b542ubn0cd02ag7j7b3gqo0r foreign key (decisions_id) references quiz_decision;
alter table if exists quiz_task_decisions add constraint FKqjmekx8ttxuvba98393chrxs1 foreign key (quiz_task_id) references quiz_task;
alter table if exists quiz_task_result add constraint FK866lrj29vheb1ypj1hqf0m3m7 foreign key (quiz_id) references quiz_result;
alter table if exists quiz_task_result add constraint FKex628dmv3kahh849m03hs4kr1 foreign key (task_id) references quiz_task;
alter table if exists user_role add constraint FKfpm8swft53ulq2hl11yplpr5 foreign key (user_id) references usr;
alter table if exists usr_quizzes add constraint FKlfd1l79rs8umlhu5pt6ar3jna foreign key (quizzes_id) references quiz;
alter table if exists usr_quizzes add constraint FKd4es4o4hu495lq1mrfl9dnl4d foreign key (usr_id) references usr;
alter table if exists usr_results add constraint FK2u0um5dhfigh6o5stupsu5gsw foreign key (results_id) references quiz_result;
alter table if exists usr_results add constraint FK2t8ptt2e7f4iybvis0v7xg7vu foreign key (usr_id) references usr;

create sequence five_variant_task_seq start with 1 increment by 50;
create sequence quiz_decision_seq start with 1 increment by 50;
create sequence quiz_result_seq start with 1 increment by 50;
create sequence quiz_seq start with 1 increment by 50;
create sequence quiz_task_result_seq start with 1 increment by 50;
create sequence quiz_task_seq start with 1 increment by 50;
create sequence usr_seq start with 1 increment by 50;
create sequence yes_or_no_task_seq start with 1 increment by 50;