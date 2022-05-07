#!/bin/bash

# restart at 5 am
while true; do
    time=$(date +"%H:%M")
    if [ ${time} = "13:18" ]; then
        echo "Restarting server..."
    fi

    sleep 60

done