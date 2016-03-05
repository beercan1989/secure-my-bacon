#!/usr/bin/env bash

##
## To be run on a local machine
##
mvn --batch-mode --settings ci/settings.xml release:clean release:prepare

## Remove release backups
mvn --batch-mode --settings ci/settings.xml release:clean