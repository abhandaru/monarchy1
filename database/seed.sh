#!/bin/bash

read -r -d '' SQL_SCRIPT << EOM
INSERT INTO users (username, email, phone_number, membership, secret) VALUES
  ('adu', 'syphondu@gmail.com', '+19787294142', 0, 'YjgwNGIxOTMxMzQ2NzhlYjdiMDdhMWZmYjZiYzUzNzliMTk5NzFmNjAzNWRmMThlNzk0N2NhY2U0YTEwNzYyYQ=='),
  ('connie', 'conniechweh@gmail.com', '+19498385435', 1, 'YjgwNGIxOTMxMzQ2NzhlYjdiMDdhMWZmYjZiYzUghs750Tk5NzFmNjAzNWRmMThlNzk0N2NhY2U0YTEwNzYyYQ==');
EOM

echo "$SQL_SCRIPT" | psql -d monarchy_local
