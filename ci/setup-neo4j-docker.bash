#!/usr/bin/env bash

set -e

NEO4J_VERSION="${1:-3.0.1}"
DOCKER_NAME='secure-my-bacon-neo4j-3'

## Create and start docker container
docker run --detach --name ${DOCKER_NAME} --publish=7474:7474 --publish=7687:7687 --env=NEO4J_AUTH=neo4j/password neo4j:${NEO4J_VERSION}

AUTH_HEADER="Authorization: Basic $(echo -n neo4j:password | base64)"
ACCEPT_HEADER="Accept: application/json; charset=UTF-8"

## Wait until Neo4j server has started
echo -n 'Waiting for Neo4j to have started: '
until curl -s -H "${AUTH_HEADER}" -H "${ACCEPT_HEADER}" 'http://localhost:7474/user/neo4j'; do
    echo -n '.'
    sleep 1s
done
echo
