#!/bin/bash
git pull
./glassfish_stop_service.sh 8000
make
make deploy
./glassfish_start_service.sh ./dist/service.war 8000 10
