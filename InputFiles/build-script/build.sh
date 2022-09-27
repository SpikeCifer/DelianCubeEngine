#!bin/bash
docker build -t database .
docker run -d --name "database-container" --publish 3306:3306 database
docker exec database-container bash /create_user.sh
