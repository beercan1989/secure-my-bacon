#!/usr/bin/env bash

## Download Neo4J
wget http://dist.neo4j.org/neo4j-community-${NEO4J_VERSION}-unix.tar.gz

## Extract Neo4J
tar -xzf neo4j-community-${NEO4J_VERSION}-unix.tar.gz

## Start Neo4J
neo4j-community-${NEO4J_VERSION}/bin/neo4j start

## Reset Neo4J Password
curl -X POST \
    -d '{ "password" : "password" }' \
    -H "Authorization: Basic $(echo -n neo4j:neo4j | base64)" \
    -H 'Accept: application/json; charset=UTF-8' \
    -H 'Content-Type: application/json' \
    'http://localhost:7474/user/neo4j/password'
