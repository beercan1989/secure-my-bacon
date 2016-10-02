#!/usr/bin/env bash

AUTH_HEADER="Authorization: Basic $(echo -n neo4j:neo4j | base64)"
ACCEPT_HEADER="Accept: application/json; charset=UTF-8"

echo 'Waiting for Neo4j to have started: '
until curl -s -H "${AUTH_HEADER}" -H "${ACCEPT_HEADER}" 'http://localhost:7474/user/neo4j'; do
    echo -n '.'
    sleep 1s
done

## Change password
curl -X POST \
  -d '{ "password" : "password" }' \
  -H "${AUTH_HEADER}" \
  -H "${ACCEPT_HEADER}" \
  -H 'Content-Type: application/json' \
  'http://localhost:7474/user/neo4j/password'

echo
