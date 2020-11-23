#!/bin/sh

rm -rf dist/
rm -rf src/ejb/generate/
rm -rf src/jmx/generate/
rm -rf src/wetdock/generate/

docker run --rm -v $(pwd):/project -w /project -u $(id -u):$(id -g) -e JAVA_HOME=/usr/lib/jvm/java-6-oracle caninjas/jdk6 ./build.sh flow-application-package