docker exec postgres pg_dump \
  -U quiz_app \
  -Fc \
  quiz_app \
  > /data/backups/postgres/quiz_app.dump