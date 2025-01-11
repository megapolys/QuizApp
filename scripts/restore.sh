#!/bin/bash

echo 'Restore database...'

ssh -i ~/.ssh/id_rsa root@185.182.111.235 << EOF

pg_restore -d postgres --data-only -a --verbose -U postgres db_backup.tar
pass #admin

EOF
echo 'Bye'

#pg_restore -d quiz_app_test_2 --data-only -a --verbose -U postgres C:/db_backup/db_backup_updated_old.tar
