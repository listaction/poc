#!/bin/bash
JAVA_PATH=~/Downloads/jdk-15.jdk/Contents/Home/bin/java
$JAVA_PATH --enable-preview -cp target/poc-1.0-SNAPSHOT-jar-with-dependencies.jar loom.poc.TestClient 
