#!/bin/bash

mvn clean package spring-boot:repackage

echo 'Copy files...'

scp -i ~/.ssh/id_rsa target/Quiz_app-0.0.1-SNAPSHOT.jar root@185.182.111.235:/root/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa root@185.182.111.235 << EOF

pgrep java | xargs kill -9
nohup java -Xms256m -Xmx512m -Dspring.profiles.active=deploy -jar Quiz_app-0.0.1-SNAPSHOT.jar > log.txt &

EOF

echo 'Bye'