#!/bin/bash

echo 'Backup database...'

DATA=$(date +"%Y-%m-%d")

ssh -i ~/.ssh/id_rsa root@185.182.111.235 << EOF

tar -cvf backup/logs_uploads_backup.tar log.txt uploads
pg_dump -f backup/db_backup.tar -F t -T flyway_schema_history -U postgres -a -W quiz
pass


EOF

echo 'Copy backup files...'

scp -i ~/.ssh/id_rsa root@185.182.111.235:/root/backup/db_backup.tar C:/db_backup/db_backup_"$DATA".tar
scp -i ~/.ssh/id_rsa root@185.182.111.235:/root/backup/logs_uploads_backup.tar C:/db_backup/logs_uploads_backup_"$DATA".tar

echo 'Bye'