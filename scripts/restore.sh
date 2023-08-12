#!/bin/bash

echo 'Restore database...'

ssh -i ~/.ssh/id_rsa root@185.182.111.235 << EOF

pg_restore -d quiz --data-only -a --verbose -U postgres backup/db_backup.tar
admin #pass

EOF
echo 'Bye'