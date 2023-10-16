alter table if exists quiz_decision rename to decision;
alter sequence if exists quiz_decision_seq rename to decision_seq;

alter table if exists yes_or_no_task rename to quiz_task_yes_or_no;
alter sequence if exists yes_or_no_task_seq rename to quiz_task_yes_or_no_seq;

alter table if exists five_variant_task rename to quiz_task_five_variant;
alter sequence if exists five_variant_task_seq rename to quiz_task_five_variant_seq;
