#!bin/bash

# Any less than 10 seconds and mysql does not manage to start in time
sleep 10
mysql --user="root" --password="1" --execute="CREATE USER 'CinecubesUser'@'%' IDENTIFIED WITH mysql_native_password BY 'Cinecubes'";
mysql --user="root" --password="1" --execute="GRANT SELECT on *.* TO 'CinecubesUser'@'%'"
