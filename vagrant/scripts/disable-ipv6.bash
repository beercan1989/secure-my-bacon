#!/usr/bin/env bash

if [ -e "/home/vagrant/.disable-ipv6-has-run" ]; then
  echo Provision step has already been run.
  exit 0
fi

export DEBIAN_FRONTEND=noninteractive
export DEBCONF_NONINTERACTIVE_SEEN=true

sudo tee --append /etc/sysctl.conf <<< "net.ipv6.conf.all.disable_ipv6 = 1"
sudo tee --append /etc/sysctl.conf <<< "net.ipv6.conf.default.disable_ipv6 = 1"
sudo tee --append /etc/sysctl.conf <<< "net.ipv6.conf.lo.disable_ipv6 = 1"

echo "/proc/sys/net/ipv6/conf/all/disable_ipv6: $(cat /proc/sys/net/ipv6/conf/all/disable_ipv6)"
echo "/proc/sys/net/ipv6/conf/default/disable_ipv6: $(cat /proc/sys/net/ipv6/conf/default/disable_ipv6)"
echo "/proc/sys/net/ipv6/conf/lo/disable_ipv6: $(cat /proc/sys/net/ipv6/conf/lo/disable_ipv6)"

sudo sysctl -p

echo "/proc/sys/net/ipv6/conf/all/disable_ipv6: $(cat /proc/sys/net/ipv6/conf/all/disable_ipv6)"
echo "/proc/sys/net/ipv6/conf/default/disable_ipv6: $(cat /proc/sys/net/ipv6/conf/default/disable_ipv6)"
echo "/proc/sys/net/ipv6/conf/lo/disable_ipv6: $(cat /proc/sys/net/ipv6/conf/lo/disable_ipv6)"
