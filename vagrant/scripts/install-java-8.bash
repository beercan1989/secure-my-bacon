#!/usr/bin/env bash
set -e

if [ -e "/home/vagrant/.install-java-8-has-run" ]; then
  echo Provision step has already been run.
  exit 0
fi

export DEBIAN_FRONTEND=noninteractive
export DEBCONF_NONINTERACTIVE_SEEN=true

sudo tee /etc/apt/sources.list.d/webupd8team-java.list <<< "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main"
sudo tee -a /etc/apt/sources.list.d/webupd8team-java.list <<< "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main"
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
sudo apt-get update

sudo /usr/bin/debconf-set-selections <<< "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true"
sudo apt-get --assume-yes install oracle-java8-installer oracle-java8-set-default oracle-java8-unlimited-jce-policy

touch /home/vagrant/.install-java-8-has-run
