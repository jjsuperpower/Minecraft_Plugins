#!/bin/bash -v

docker build -t jjsuperpower/rlc_testing .

docker run --rm -e RLC_VERSION=2.8.2 -e EULA=true -it jjsuperpower/rlc_testing