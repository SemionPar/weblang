#!/bin/bash

mkdir temp
cd temp
7z x ../build/libs/weblang-0.0.1-SNAPSHOT.jar
cat META-INF/MANIFEST.MF ../MANIFEST.MF > META-INF/MANIFEST.MF
7z u ../build/libs/weblang-0.0.1-SNAPSHOT.jar *
cd ..
rm -rf temp
