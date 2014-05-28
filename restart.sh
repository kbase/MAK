#!/bin/bash
git pull
./stop_service
make
make deploy
./start_service
