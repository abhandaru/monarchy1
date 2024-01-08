#!/bin/bash

read -r -d '' SQL_SCRIPT << EOM
INSERT INTO users (username, phone_number, membership, secret) VALUES
  ('adu', '+19787294142', 0, 'YjgwNGIxOTMxMzQ2NzhlYjdiMDdhMWZmYjZiYzUzNzliMTk5NzFmNjAzNWRmMThlNzk0N2NhY2U0YTEwNzYyYQ=='),
  ('connie', '+19498385435', 1, 'YjgwNGIxOTMxMzQ2NzhlYjdiMDdhMWZmYjZiYzUghs750Tk5NzFmNjAzNWRmMThlNzk0N2NhY2U0YTEwNzYyYQ==');
EOM

echo "$SQL_SCRIPT" | psql -d monarchy_local
