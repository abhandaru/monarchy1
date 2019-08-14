#!/bin/bash

ROOT=$(git rev-parse --show-toplevel)

cd $ROOT

# Get VIEW SQL
SQL_SCRIPT=$(cat ./database/1-init.sql)

# echo "[views/local] saving locally"
# echo "$SQL_SCRIPT" | psql -d monarchy_local
# echo "[views/staging] saving to staging"
# echo "$SQL_SCRIPT" | heroku pg:psql -a monarchy1-staging
echo "[views/prod] saving to prod"
echo "$SQL_SCRIPT" | heroku pg:psql -a monarchy1
