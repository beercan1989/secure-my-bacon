#!/usr/bin/env bash

set +e

## Decrypt the encrypted private GPG key
openssl aes-256-cbc -pass env:GPG_CIPHER_PASSWORD -in ci/gpg/private-key.asc.enc -out ci/gpg/private-key.asc -d

## Import the decrypted private GPG key
gpg --homedir ci/gpg --import ci/gpg/private-key.asc

## Delete the decrypted private GPG key
rm -v ci/gpg/private-key.asc

## Run the standard maven deploy task
mvn --batch-mode --settings travis/settings.xml --activate-profiles release -DskipTests -DskipITs deploy

## Clean up the GPG directory, so we don't leave anything behind
find ci/gpg -mindepth 1 -delete

## Return the maven exit code, so we can report if maven failed
MAVEN_EXIT_CODE="${?}"
if [ ${MAVEN_EXIT_CODE} -ne 0 ]; then
    echo
    echo "Maven exited with error code [${MAVEN_EXIT_CODE}]."
    echo

    exit ${MAVEN_EXIT_CODE}
fi
