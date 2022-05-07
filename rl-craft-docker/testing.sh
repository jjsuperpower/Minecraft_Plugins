#!/bin/bash -v

docker build -t jjsuperpower/rlc_testing .

docker run --rm -e RLC_VERSION=2.8.1 -e EULA=TRUE -it jjsuperpower/rlc_testing