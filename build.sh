#!/bin/bash
set -e

# Répertoires de sortie
OUTPUT_DIR=build/WEB-INF/classes
FRAMEWORK_JAR="framework.jar"

# Compilation
javac -d $OUTPUT_DIR -cp lib/javaee-api-8.0.jar:lib/jboss-vfs-3.3.1.Final.jar src/main/java/com/ETU1792/annotation/*.java
javac -d $OUTPUT_DIR -cp "$OUTPUT_DIR:lib/javaee-api-8.0.jar:lib/jboss-vfs-3.3.1.Final.jar" src/main/java/com/ETU1792/utils/*.java
javac -d $OUTPUT_DIR -cp "$OUTPUT_DIR:lib/javaee-api-8.0.jar:lib/jboss-vfs-3.3.1.Final.jar" src/main/java/com/ETU1792/controller/*.java

# Creation du JAR
jar -cvf $FRAMEWORK_JAR -C build/WEB-INF/classes/ .