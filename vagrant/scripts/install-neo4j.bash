#!/usr/bin/env bash
set -ev

wget -O - https://debian.neo4j.org/neotechnology.gpg.key | sudo apt-key add -
sudo tee /etc/apt/sources.list.d/neo4j.list <<< "deb http://debian.neo4j.org/repo stable/"
sudo apt-get update

sudo apt-get --assume-yes install neo4j
