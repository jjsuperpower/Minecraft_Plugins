#!/bin/bash -v

docker build -t jjsuperpower/rl-craft:0.0.1 .

docker run --rm -e RLC_VERSION=2.8.1 -e EULA=TRUE -p 25565:25565 -it jjsuperpower/rlc_testing