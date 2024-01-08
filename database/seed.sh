#!/bin/bash

read -r -d '' SQL_SCRIPT << EOM
INSERT INTO users (username, phone_number, secret) VALUES
  ('adu', '+19787294142', 'YjgwNGIxOTMxMzQ2NzhlYjdiMDdhMWZmYjZiYzUzNzliMTk5NzFmNjAzNWRmMThlNzk0N2NhY2U0YTEwNzYyYQ=='),
  ('opponent', '+19498385435', 'YjgwNGIxOTMxMzQ2NzhlYjdiMDdhMWZmYjZiYzUghs750Tk5NzFmNjAzNWRmMThlNzk0N2NhY2U0YTEwNzYyYQ==');
EOM

echo "$SQL_SCRIPT" | psql -d monarchy_local
