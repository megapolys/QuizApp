#!/bin/bash

echo 'Restore database...'

ssh -i ~/.ssh/id_rsa root@185.182.111.235 << EOF

pg_restore -d postgres --data-only -a --verbose -U postgres db_backup.tar
pass #admin

EOF
# pg_restore -d postgres --data-only -a --verbose -U postgres db_backup.tar
echo 'Bye'