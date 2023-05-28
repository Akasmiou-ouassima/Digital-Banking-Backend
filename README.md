<img src="icon.png" align="right" />

 ## Digital-Banking-Project 🔗 
 
>**Le travail à effectuer consiste à développer une application Web basée sur Spring et Angular pour la gestion de comptes bancaires.**

## Diagramme de cas d'utilisation du projet Digital banking
<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/Diagramme%20de%20classe%20digital%20banking.jpg">

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



  ## Architecture du projet
<div>
   <img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/architecture2.png" alt="image2"  width="30%">
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/architecture1.jpg" alt="image1" style="margin-top:1px;" width="45%">
  
</div>

#### Couche DAO
> Les entités JPA : Customer, BankAccount, Saving Account, CurrentAccount, AccountOperation
 _Customer_
```@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "customer")
    private List<BankAccount> bankAccounts;
}
```
 _BankAccount_
```@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 30, discriminatorType = DiscriminatorType.STRING) // length 255 par défaut et String
@Data @NoArgsConstructor @AllArgsConstructor
public abstract class BankAccount {
 @Id
    private String id;
    private double balance;
    private Date createdDate;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne
    private Customer customer;
    @OneToMany (mappedBy = "bankAccount")
    private List<AccountOperation> accountOperations;

}
```
_Saving Account_
```@Entity
@DiscriminatorValue("Saving_Account")
@Data @NoArgsConstructor @AllArgsConstructor
public class SavingAccount extends BankAccount{
    private double interestRate;
}
```
_CurrentAccount_
```@Entity
@DiscriminatorValue("Current_Account")
@Data @NoArgsConstructor @AllArgsConstructor
public class CurrentAccount extends BankAccount{
    private double overDraft;

}
```
_AccountOperation_
```@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AccountOperation {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    @ManyToOne
    private BankAccount bankAccount;
    private String description;
}
```


