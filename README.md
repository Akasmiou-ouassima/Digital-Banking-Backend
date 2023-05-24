|       Digital-Banking-Project           |
| --------------------------------------- |

## Diagramme de cas d'utilisation du projet Digital banking
![Diagramme de classe digital banking](https://github.com/Akasmiou-ouassima/Digital-Banking-Project/assets/105364446/4bef82b9-e2a3-4806-8091-3ab5218ee56e)

Le travail à effectuer consiste à développer une application Web basée sur Spring et Angular pour la gestion de comptes bancaires. Voici les différentes tâches à réaliser pour la première partie du projet :

**1. Création et test de la couche DAO :**
<ul>
  <li>Définir les entités JPA pour les comptes et les clients.</li>
 <li>Implémenter les repositories pour accéder aux données des comptes et des clients. </li>
 <li>Tester les opérations CRUD (Create, Read, Update, Delete) pour les entités. </li>
</ul>

**2. Création et test de la couche service :**
<ul>
  <li>Définir les opérations du service pour ajouter des comptes, ajouter des clients, effectuer des débits, des crédits et des virements, et consulter un compte.
.</li>
 <li>Implémenter la logique métier pour ces opérations. </li>
</ul>

**3. Création et test de la couche Web (Rest Controller) :**
<ul>
  <li>Créer les contrôleurs REST pour exposer les fonctionnalités de l'application.</li>
 <li>Définir les endpoints REST pour les opérations du service. </li>
 <li>Tester les endpoints à l'aide d'outils comme Postman ou Swagger.</li>
</ul>

**4. Modification de la couche service et de la couche Web en utilisant les DTO :**
<ul>
  <li>Créer des objets DTO (Data Transfer Objects) pour transférer les données entre les couches.</li>
 <li>Adapter les services et les contrôleurs pour utiliser les DTO. </li>
</ul>

**5. Création d'un service d'authentification séparé basé sur Spring Security et JWT :**
<ul>
  <li>Configurer Spring Security pour gérer l'authentification des utilisateurs.</li>
 <li>Utiliser JWT (JSON Web Tokens) pour générer et valider les tokens d'authentification.</li>
 <li>Implémenter les endpoints nécessaires pour l'inscription et la connexion des utilisateurs.</li>
</ul>

**6. Sécurisation de l'application Digital Banking en utilisant Spring Security et JWT :**
<ul>
  <li>Appliquer les configurations de sécurité de Spring Security pour restreindre l'accès aux ressources de l'application.</li>
 <li>Configurer les autorisations en fonction des rôles des utilisateurs.</li>
 <li>Tester l'authentification et l'autorisation pour s'assurer du bon fonctionnement de la sécurité.</li>
</ul>

**7. Création de la partie Frontend Web en utilisant Angular :**
<ul>
  <li>Concevoir et développer les composants Angular pour l'interface utilisateur de l'application..</li>
 <li>Intégrer les appels API vers les endpoints REST du backend pour récupérer et modifier les données.</li>
 <li>Implémenter les fonctionnalités de gestion des comptes bancaires, d'authentification et de sécurité.</li>
  <li>Tester l'interface utilisateur et s'assurer de son bon fonctionnement.</li>
</ul>

**Ces étapes vous permettront de mettre en place les différentes couches de l'application, de tester leur fonctionnement et de sécuriser l'application en utilisant Spring Security et JWT. Vous pourrez ainsi créer une application complète de gestion de comptes bancaires avec une interface utilisateur conviviale basée sur Angular.**

  ## Architecture du projet
<div>
  
  <img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/assets/105364446/13602251-007b-4b7b-90a2-cad759288f83" alt="image1" width="45%">
  
  <img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/assets/105364446/de2d2959-29be-4ffb-95df-a92f249f9435" alt="image2" width="45%">
</div>


