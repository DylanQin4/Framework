# Sprints

## Sprint 0
### Objectif
Créer un servlet qui réceptionnera toutes les requêtes clients et qui les traitera.

### Étapes
- **Côté Framework**:
  - Créer un servlet `FrontController` dont la méthode `processRequest` affichera l'URL dans lequel on se trouve.
- **Côté Test**:
  - Associer le `FrontController` à l'URL pattern `/` dans le `web.xml` du projet.
  - Tester n'importe quelle URL associée au projet web.

### Git
- Créer un projet Git.
- Cloner localement.
- Créer une branche pour le sprint actuel nommé: `sprint[n° sprint]-[etu]`.
- À la fin du sprint, envoyer un "Merge request" du sprint et supprimer la branche.

## Sprint 1
### Objectif
Envoyer le framework sur Git.

### Étapes
- **Modifications dans le framework**:
  - Créer `AnnotationController`.
  - Annoter les contrôleurs avec `AnnotationController`.
  - Mettre les contrôleurs dans le même package.
- **Modifications dans `FrontController`**:
  - Tester si les contrôleurs ont déjà été scannés.
    - Si oui, afficher la liste des noms des contrôleurs.
    - Sinon, scanner, puis afficher la liste des noms des contrôleurs.
- **Modifications dans le projet de test**:
  - Déclarer le nom du package contenant les contrôleurs dans `web.xml` (using `init-param`).
  - Déclarer `FrontServlet`.
- Créer un fichier `README` pour décrire précisément les configurations à faire pour utiliser le framework.

## Sprint 2
### Objectif
Récupérer la classe et la méthode associées à une URL donnée.

### Étapes
- Créer une annotation `@GET` pour annoter les méthodes dans les contrôleurs.
- Créer la classe `Mapping` avec les attributs `String className` et `String methodName`.
- **Modifications dans `FrontController`**:
  - Enlever l'attribut boolean.
  - Créer un `HashMap<String, Mapping>`.
  - Initialiser:
    - Scanner pour obtenir les contrôleurs.
    - Pour chaque contrôleur, prendre toutes les méthodes et vérifier s'il y a l'annotation `@GET`.
    - S'il y en a, créer un nouveau `Mapping` : `(controller.name, method.name)`.
    - Associer `annotation.value` à `Mapping` dans le `HashMap`.
  - `processRequest`:
    - Prendre le `Mapping` associé au chemin URL de la requête.
    - Si on trouve le `Mapping` associé, afficher le chemin URL et le `Mapping`.
    - Sinon, afficher qu'il n'y a pas de méthode associée à ce chemin.

## Sprint 3
### Objectif
Exécuter la méthode de la classe associée à une URL donnée.

### Étapes
- **Dans `FrontController` (`processRequest`)**:
  - Si on trouve le `Mapping` associé à l'URL:
    - Récupérer la classe par son nom.
    - Récupérer la méthode par son nom.
    - Invoquer la méthode sur l'instance de la classe.
    - Afficher la valeur retournée par la méthode.
- **Projet Test**:
  - Les méthodes des contrôleurs annotées ont pour type de retour `String`.

## Sprint 4
### Objectif
Envoyer des données du contrôleur vers la vue.

### Étapes
- **Côté Framework**:
  - Créer une classe `ModelView` avec les attributs:
    - `String url` (URL de destination après l'exécution de la méthode).
    - `HashMap<String, Object> data` (données à envoyer vers cette vue).
  - Créer une fonction `addObject` pour ajouter des données dans le `HashMap`.
- **Modifications dans `FrontController`**:
  - Dans `processRequest`, récupérer les données issues de la méthode annotée `@GET`:
    - Si les données sont de type `String`, retourner la valeur directement.
    - Si les données sont de type `ModelView`, récupérer l'URL et dispatcher les données vers cet URL.
    - Boucler sur les données et les ajouter à la requête avec `request.setAttribute`.
    - Si autre, retourner "non reconnu".
- **Test**:
  - Les méthodes des contrôleurs annotées ont pour type de retour `String` ou `ModelView`.

## Sprint 5
### Objectif
Gestion d'exception.

### Étapes
- **Building**:
  - Exception si une annotation est dupliquée (plusieurs méthodes ont la même annotation).
  - Exception si le package des contrôleurs est vide ou n'existe pas.
- **Process**:
  - Exception "Error 404 not found" si l'URL n'existe pas.
  - Exception si le type de retour de la méthode du contrôleur n'est pas un `String` ou un `ModelView`.

## Sprint 6
### Objectif
Envoyer des données d'un formulaire vers un contrôleur.

### Étapes
- **Côté Framework**:
  - Créer une annotation `@Param` avec l'attribut `String name`.
  - Ajouter un argument `HttpServletRequest request` dans la fonction invoquant les méthodes des contrôleurs.
  - Boucler sur les arguments de la méthode du contrôleur et récupérer les noms des annotations `@Param` de chaque argument.
  - Attribuer la valeur de chaque argument en utilisant `request.getParameter` avec le nom de son annotation comme argument.
- **Test**:
  - Créer un formulaire d'envoi (ex : Entrer votre nom).
  - Créer une méthode dans un contrôleur pour récupérer le nom entré.
  - Renvoyer un `ModelView` pour vérifier si le nom est bien récupéré.
- **Note**:
  - Pour les liens GET tels que "emplist?ville=105":
    - Lors de la récupération du `Mapping`, enlever le texte après "?" et utiliser le lien avant "?".

## Sprint 7
### Objectif
Permettre de mettre en paramètre d'une fonction de mapping un objet et de configurer ses attributs.

### Étapes
1. Créer une annotation pour l'objet en paramètre.
2. Créer un processus qui s'exécute automatiquement lorsque le programme détecte l'annotation créée.
   - Ce processus boucle sur tous les attributs de l'objet pour obtenir leurs valeurs attribuées dans `request.getParameter`.
   - Créer une nouvelle annotation de type `ElementType.FIELD` pour donner le choix aux utilisateurs du framework d'utiliser le même nom dans leur classe et leur formulaire ou d'annoter l'attribut avec le nom présent dans leur formulaire sans devoir utiliser le même nom.

## Sprint 8
### Objectif
Gestion et utilisation de session.

### Étapes
- **Côté Framework**:
  - Créer une classe `MySession` ayant comme seul attribut `HttpSession session`.
  - Ajouter les fonctions `get(String key)`, `add(String key, Object objet)`, `delete(String key)`.
  - À l'appel des méthodes des contrôleurs de l'utilisateur, pendant la génération des arguments, vérifier si le paramètre est de type `MySession` et dans ce cas, créer un `MySession` avec `req.getSession()`.
- **Côté Test**:
  - Créer un formulaire de login (identifiant, mot de passe).
  - Quand la personne se connecte, elle accède à une liste de données propres à son identifiant.
  - Ajouter un bouton déconnexion qui supprime les données de la session.
  - Vous pouvez utiliser n'importe quel type pour les listes de données mais sans utiliser de système de base de données.

## Sprint 9
### Objectif
Exposition de méthodes de contrôleur qui renvoient des données en JSON (REST API).

### Étapes
- **Côté Framework**:
  - Créer une annotation `@JSON`.
  - Cette annotation sera utilisée pour indiquer que la méthode du contrôleur doit renvoyer des données au format JSON, sans passer par une vue.

## Sprint 11
### Objectif
Gestion d'exception dans une page web.

### Étapes
- Afficher les exceptions gérées précédemment dans le Sprint 5 dans une page web sous forme de `String` en utilisant `response.getWriter()` et `response.setContentType("text/html")`.

## Sprint 12
### Objectif
Permettre la récupération de fichier par formulaire.

### Étapes
- Annoter par l'annotation `@MultipartConfig` la classe `FrontController`.
- Dans la méthode `prepareMethodParameters`, ajouter une condition que si le paramètre de la méthode du contrôleur est de type `Part`, alors au lieu de `request.getParameter`, on récupère la valeur par `request.getPart`.

## Sprint 13
### Objectif
Validation des données formulaire.

### Étapes
- Ajouter des annotations de validation pour les champs des objets et gérer les erreurs de validation.

## Sprint 14
### Objectif
Validation des données avec retour des erreurs.

### Étapes
- Au lieu de lancer une exception, renvoyer les erreurs et les valeurs des inputs vers le formulaire.

## Sprint 15
### Objectif
Sécurisation des méthodes avec les annotations `@Authentified` et `@Role`.

### Étapes
- Ajouter les annotations `@Authentified` et `@Role` aux méthodes pour les sécuriser.

## Sprint 16
### Objectif
Permettre l'ajout des annotations `@Authentified` et `@Role` au niveau des classes.

### Étapes
- Modifier le `FrontController` pour vérifier les annotations `@Authentified` et `@Role` au niveau des classes et des méthodes.