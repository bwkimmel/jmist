#!/bin/bash

cd "$( dirname "${BASH_SOURCE[0]}" )"

docker build .
docker run --rm -v $PWD/..:/src $(docker build -q .) make "$@"
