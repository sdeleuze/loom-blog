#!/usr/bin/env bash

set -e
source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk env
mvn clean package
java --enable-preview -jar target/loom-blog-jar-with-dependencies.jar