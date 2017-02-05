#!/usr/bin/env bash

set -e

if [ "${NEO4J_DRIVER}" = "org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver" ]; then
    echo "Skipping Neo4J setup as driver type is: ${NEO4J_DRIVER}"
    exit 0
fi

NEO4J_VERSION="${1:-${NEO4J_VERSION:-3.0.1}}"
NEO4J_PASSWORD="${2:-${NEO4J_PASSWORD:-password}}"
NEO4J_DEFAULT_PASSWORD="${3:-${NEO4J_DEFAULT_PASSWORD:-neo4j}}"

## Download
wget "http://dist.neo4j.org/neo4j-community-${NEO4J_VERSION}-unix.tar.gz"

## Extract
tar -xzf neo4j-community-${NEO4J_VERSION}-unix.tar.gz

## Start in a detached mode
neo4j-community-${NEO4J_VERSION}/bin/neo4j start

## Default Neo4j credentials
AUTH_HEADER="Authorization: Basic $(echo -n neo4j:${NEO4J_DEFAULT_PASSWORD} | base64)"
ACCEPT_HEADER="Accept: application/json; charset=UTF-8"

## Wait until Neo4j has started up
echo 'Waiting for Neo4j to have started: '
until curl -s -H "${AUTH_HEADER}" -H "${ACCEPT_HEADER}" 'http://localhost:7474/user/neo4j'; do
    echo -n '.'
    sleep 1s
done

## Change Neo4j password
curl -X POST \
  -d '{ "password" : "'${NEO4J_PASSWORD}'" }' \
  -H "${AUTH_HEADER}" \
  -H "${ACCEPT_HEADER}" \
  -H 'Content-Type: application/json' \
  'http://localhost:7474/user/neo4j/password'

echo