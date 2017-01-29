#!/usr/bin/env bash
set -e

if [ -e "/home/vagrant/.install-neo4j-has-run" ]; then
  echo Provision step has already been run.
  exit 0
fi

export DEBIAN_FRONTEND=noninteractive
export DEBCONF_NONINTERACTIVE_SEEN=true
export NEO4J_VERSION="3.0.1"

wget -O - https://debian.neo4j.org/neotechnology.gpg.key | sudo apt-key add -
sudo tee /etc/apt/sources.list.d/neo4j.list <<< "deb http://debian.neo4j.org/repo stable/"
sudo apt-get update

sudo apt-get --assume-yes install curl neo4j=${NEO4J_VERSION}

## Enable external connections
sed -i 's/^# *dbms.connector.bolt.address=0.0.0.0:7687/dbms.connector.bolt.address=0.0.0.0:7687/' /etc/neo4j/neo4j.conf
sed -i 's/^# *dbms.connector.http.address=0.0.0.0:7474/dbms.connector.http.address=0.0.0.0:7474/' /etc/neo4j/neo4j.conf

AUTH_HEADER="Authorization: Basic $(echo -n neo4j:neo4j | base64)"
ACCEPT_HEADER="Accept: application/json; charset=UTF-8"

echo 'Waiting for Neo4j to have started: '
until curl -s -H "${AUTH_HEADER}" -H "${ACCEPT_HEADER}" 'http://localhost:7474/user/neo4j'; do
    echo -n '.'
    sleep 1s
done

## Change default user:password from [neo4j/neo4j] http://neo4j.com/docs/stable/rest-api-security.html
curl -X POST \
  -d '{ "password" : "password" }' \
  -H "${AUTH_HEADER}" \
  -H "${ACCEPT_HEADER}" \
  -H 'Content-Type: application/json' \
  'http://localhost:7474/user/neo4j/password'

## Restart Neo4J to pick up configuration changes.
sudo systemctl restart neo4j

touch /home/vagrant/.install-neo4j-has-run
