#!/bin/bash -v

docker build -t jjsuperpower/rl-craft:0.0.1 .

# docker run --rm -e RLC_VERSION=2.8.2 -e EULA=TRUE -p 25565:25565 -it jjsuperpower/rl-craft:0.0.1

mkdir $(pwd)/rlc-server
docker run --rm --name rlc-server -v "$(pwd)/rlc-server":/minecraft -e RLC_VERSION=2.8.2 -e EULA=TRUE -p 25565:25565 -it jjsuperpower/rl-craft:0.0.1