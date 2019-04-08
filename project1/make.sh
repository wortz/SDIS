#!/usr/bin/env bash
rm -rf bin
rm -f *.jar
mkdir -p bin
javac -d bin -sourcepath src src/client/TestApp.java src/Peer.java