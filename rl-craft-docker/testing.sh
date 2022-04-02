#!/bin/bash -v

docker build -t jjsuperpower/rlc_testing .

docker run --rm -e RLC_VERSION=2.8.2 -it jjsuperpower/rlc_testing