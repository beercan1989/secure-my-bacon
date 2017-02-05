#!/usr/bin/env bash

set -e

echo
echo "Setting up Neo4j for:"
echo "   NEO4J_URI: ${NEO4J_URI}"
echo "   NEO4J_DRIVER: ${NEO4J_DRIVER}"
echo "   NEO4J_VERSION: ${NEO4J_VERSION}"
echo "   NEO4J_USERNAME: ${NEO4J_USERNAME}"
echo "   NEO4J_PASSWORD: ${NEO4J_PASSWORD}"
echo "   NEO4J_DEFAULT_PASSWORD: ${NEO4J_DEFAULT_PASSWORD}"
echo

if [ "${NEO4J_DRIVER}" = "org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver" ]; then
    echo "Skipping Neo4J setup as driver type is: ${NEO4J_DRIVER}"
    exit 0
fi

NEO4J_VERSION="${1:-${NEO4J_VERSION:-3.0.1}}"
NEO4J_PASSWORD="${2:-${NEO4J_PASSWORD:-password}}"

DOCKER_NAME="secure-my-bacon-neo4j-${NEO4J_VERSION}"

## Download required docker image
docker pull "neo4j:${NEO4J_VERSION}"

## Create and start docker container
docker run --detach --name ${DOCKER_NAME} --publish=7474:7474 --publish=7687:7687 --env="NEO4J_AUTH=neo4j/${NEO4J_PASSWORD}" "neo4j:${NEO4J_VERSION}"

## Neo4j credentials
AUTH_HEADER="Authorization: Basic $(echo -n neo4j:${NEO4J_PASSWORD} | base64)"
ACCEPT_HEADER="Accept: application/json; charset=UTF-8"

## Wait until Neo4j server has started
echo -n 'Waiting for Neo4j to have started: '
until curl -s -H "${AUTH_HEADER}" -H "${ACCEPT_HEADER}" 'http://localhost:7474/user/neo4j'; do
    echo -n '.'
    sleep 1s
done

echo
