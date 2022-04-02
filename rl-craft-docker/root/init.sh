#!/bin/bash

source init.d/mc-init.sh

#check if RLC_VERSION is empty
if [ -z "${RLC_VERSION}" ]; then
    echo "No RLC_VERSION specified, exiting"
    exit 1
fi

# get required urls
get_rlcraft_url ${RLC_VERSION}
check_versions

download_rl_craft
download_forge
