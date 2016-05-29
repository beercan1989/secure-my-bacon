#!/usr/bin/env bash

set -e

## Download Neo4J
wget http://dist.neo4j.org/neo4j-community-${NEO4J_VERSION}-unix.tar.gz

## Extract Neo4J
tar -xzf neo4j-community-${NEO4J_VERSION}-unix.tar.gz

## Start Neo4J
neo4j-community-${NEO4J_VERSION}/bin/neo4j start

AUTH_HEADER="Authorization: Basic $(echo -n neo4j:neo4j | base64)"
ACCEPT_HEADER="Accept: application/json; charset=UTF-8"

## Wait until Neo4j server has started
function neo4j-responding {
    curl -H "Authorization: Basic $(echo -n neo4j:neo4j | base64)" \
         -H 'Accept: application/json; charset=UTF-8' \
         'http://localhost:7474/user/neo4j'
}
echo 'Waiting for Neo4j to have started: '
until curl -H "${AUTH_HEADER}" -H "${ACCEPT_HEADER}" 'http://localhost:7474/user/neo4j'; do
    echo -n '.'
    sleep 1s
done

## Reset Neo4J Password
curl -X POST \
    -d '{ "password" : "password" }' \
    -H "${AUTH_HEADER}" \
    -H "${ACCEPT_HEADER}" \
    -H 'Content-Type: application/json' \
    'http://localhost:7474/user/neo4j/password'
