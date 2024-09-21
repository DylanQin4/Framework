#!/bin/bash
set -e
# Chargement des variables d'environnement depuis .env
source .env

# Compile Java
javac -d build/WEB-INF/classes/ -cp lib/javaee-api-8.0.jar src/main/java/com/ETU1792/controller/*.java

# Generer le fichier WAR
jar -cvf framework.war -C build/ . -C src/main/webapp .

# Deploiement du war vers le serveur
cp framework.war "$DEPLOYMENT_SERVER"