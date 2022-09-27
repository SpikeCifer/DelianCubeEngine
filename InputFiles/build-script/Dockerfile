FROM mysql
# Copy backup files in image and load them to db
COPY backup-files/* /docker-entrypoint-initdb.d/
COPY create_user.sh /
ENV MYSQL_ROOT_PASSWORD 1
EXPOSE 3306
