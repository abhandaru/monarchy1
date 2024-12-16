#!/bin/bash

KEY=monarchy/streaming/game/$1
echo "get $KEY" | redis-cli | jq
