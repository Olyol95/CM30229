#!/usr/bin/sh
mkdir -p out/production
mkdir -p out/deployment
$NXJ_HOME/bin/nxjc -d out/production/ -cp src src/com/bjdody/cm30229/$1.java
$NXJ_HOME/bin/nxjlink -o out/deployment/$1.nxj -cp out/production/ com.bjdody.cm30229.$1