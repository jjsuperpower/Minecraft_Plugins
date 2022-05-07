#!/bin/bash

source /mc_init.d/mc_create.sh

mc_create_server

cd ${MC_INST_DIR}
java -jar server.jar -Xmx${MEM_SIZE} -Xms${MEM_SIZE}

exit_code=$?

echo "Java exited with code ${exit_code}"