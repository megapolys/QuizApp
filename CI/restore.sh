docker exec -i postgres pg_restore \
  -U quiz_app \
  -d quiz_app \
  /data/backups/postgres/quiz_app.dump