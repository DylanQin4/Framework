```

mvn install:install-file -Dfile=lib/framework.jar -DgroupId=com.ETU1792 -DartifactId=LohataonaFramework -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=lib/gson-2.11.0.jar -DgroupId=com.google.code.gson -DartifactId=gson -Dversion=2.11.0 -Dpackaging=jar

mvn install:install-file -Dfile=lib/javaee-api-8.0.jar -DgroupId=javax -DartifactId=javaee-api -Dversion=8.0 -Dpackaging=jar

mvn install:install-file -Dfile=lib/jboss-vfs-3.3.1.Final.jar -DgroupId=org.jboss -DartifactId=jboss-vfs -Dversion=3.3.1.Final -Dpackaging=jar

mvn install:install-file -Dfile=lib/paranamer-2.8.jar -DgroupId=com.thoughtworks.paranamer -DartifactId=paranamer -Dversion=2.8 -Dpackaging=jar

```

3. Créez le Dossier d'upload file sur votre pc ou le serveur
```bash
sudo mkdir -p /var/itu/LohataonaFramework/uploads
sudo chown -R <votre_utilisateur>:<votre_groupe> /var/itu/LohataonaFramework/uploads
sudo chmod -R 755 /var/itu/LohataonaFramework/uploads
```