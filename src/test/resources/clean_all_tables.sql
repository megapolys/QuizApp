DO
'
DECLARE
    query_1 text;
BEGIN
FOR query_1 IN select t.table_name
from information_schema.tables t
where t.table_schema = ''quiz_app''
    and t.table_type = ''BASE TABLE''
    LOOP
    EXECUTE ''TRUNCATE quiz_app.'' || query_1 || '' cascade;'';
    END LOOP;
END;
';