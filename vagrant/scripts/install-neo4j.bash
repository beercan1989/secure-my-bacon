#!/usr/bin/env bash
set -ev

if [ -s "/home/vagrant/.install-neo4j-has-run" ]; then
  echo
  echo Provision step has already been run.
  echo
  exit 0
fi

export DEBIAN_FRONTEND=noninteractive
export DEBCONF_NONINTERACTIVE_SEEN=true

wget -O - https://debian.neo4j.org/neotechnology.gpg.key | sudo apt-key add -
sudo tee /etc/apt/sources.list.d/neo4j.list <<< "deb http://debian.neo4j.org/repo stable/"
sudo apt-get update

sudo apt-get --assume-yes install neo4j

# Enable external connections
sed -i 's/#org.neo4j.server.webserver.address=0.0.0.0/org.neo4j.server.webserver.address=0.0.0.0/' /var/lib/neo4j/conf/neo4j-server.properties

# TODO: Setup users that can access Neo4J via port 7474

# Restart Neo4J to pick up configuration changes.
sudo /etc/init.d/neo4j-service restart

touch /home/vagrant/.install-neo4j-has-run
