# Framework Java
Ce projet est un framework Java pour la gestion des requêtes HTTP via des servlets. Il utilise des annotations pour définir les contrôleurs et les méthodes de traitement des requêtes

## Requis et Installation

### Prérequis
- Java 8 ou supérieur
- Apache Maven
- Un serveur d'application compatible avec les servlets (par exemple, Apache Tomcat)

### Installation
1. Configurez la variable `DEPLOYMENT_SERVER` dans le fichier `.env` :
    ```properties
    DEPLOYMENT_SERVER=<chemin_vers_votre_serveur_de_deploiement>
    ```

2. Exécutez le script de build :
    ```bash
    ./build.sh
    ```

## Fonctionnalités du Framework

### 1. Annotations
Le framework utilise des annotations pour définir les contrôleurs et les méthodes de traitement des requêtes.

- **@Controller** : Marque une classe comme étant un contrôleur.
- **@GET** et **@POST** : Marquent les méthodes des contrôleurs qui doivent être associées aux requêtes HTTP GET et POST.
- **@Param** : Utilisée pour annoter les paramètres des méthodes des contrôleurs afin de les lier aux paramètres des requêtes HTTP.
- **@ParamObject** : Utilisée pour annoter les paramètres des méthodes des contrôleurs qui sont des objets, permettant de remplir automatiquement leurs attributs à partir des paramètres des requêtes HTTP.
- **@FieldName** : Utilisée pour spécifier un nom de champ différent pour les attributs des objets.
- **@JSON** : Indique que la méthode du contrôleur doit renvoyer des données au format JSON.
- **@Authentified** : Indique que la méthode nécessite une authentification.
- **@Role** : Indique que la méthode nécessite un rôle spécifique pour être exécutée.
- **Annotations de validation** : @Required, @Email, @Date, @Numeric pour valider les champs des objets.

### 2. Gestion des Sessions
Le framework permet de gérer les sessions HTTP via la classe `MySession`.

### 3. Validation des Données
Les champs des objets peuvent être validés en utilisant les annotations de validation.

### 4. Gestion des Erreurs
Les erreurs sont gérées et affichées dans une page web.

### 5. Support des Fichiers
Le framework permet de gérer les fichiers envoyés via des formulaires en utilisant l'annotation `@MultipartConfig`.

### 6. Authentification et Rôles
Le framework gère l'authentification et les rôles avant d'exécuter les méthodes des contrôleurs.
