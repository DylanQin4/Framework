#!/bin/bash
set -e

# Chargement des variables d'environnement depuis .env
source .env

GENERAL_PATH="$DEPLOYMENT_SERVER"

# Copie du fichier WAR vers le serveur de deploiement
cp -f "target/Avion.war" "$GENERAL_PATH"

echo "Le fichier WAR a ete deploye dans $GENERAL_PATH"