@ECHO off
set file=%1
if not exist out mkdir out
if not exist out\production mkdir out\production
if not exist out\deployment mkdir out\deployment
call "%NXJ_HOME%\bin\nxjc.bat" -d out/production/ -cp src src/com/bjdody/cm30229/%file%.java
call "%NXJ_HOME%\bin\nxjlink.bat" -o out/deployment/%file%.nxj -cp out/production/ com.bjdody.cm30229.%file%