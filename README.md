<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/icon.png" align="right" />

 ## 🔗  Digital-Banking-Project
 
>**Le travail à effectuer consiste à développer une application Web basée sur Spring et Angular pour la gestion de comptes bancaires.**

## Diagramme de cas d'utilisation du projet Digital banking
<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/Diagramme%20de%20classe%20digital%20banking.jpg">

**1. Création et test de la couche DAO :**
<ul>
  <li>Définir les entités JPA </li>
 <li>Créer les interfaces JPA Repository basées sur Spring Data </li>
 <li>Tester la couche DAO</li>
</ul>

**2. Création et test de la couche service :**
<ul>
  <li>Définir les opérations du service pour ajouter des comptes, ajouter des clients, effectuer des débits, des crédits et des virements, et consulter un compte
.</li>
 <li>Implémenter la logique métier pour ces opérations </li>
  <li>Tester les opérations CRUD (Create, Read, Update, Delete) pour les entités </li>
</ul>

**3. Création et test de la couche Web (Rest Controller) :**
<ul>
  <li>Créer les contrôleurs REST pour exposer les fonctionnalités de l'application</li>
 <li>Définir les endpoints REST pour les opérations du service</li>
 <li>Tester les endpoints à l'aide d'outils comme Postman ou Swagger</li>
</ul>

**4. Modification de la couche service et de la couche Web en utilisant les DTO :**
<ul>
  <li>Créer des objets DTO (Data Transfer Objects) pour transférer les données entre les couches</li>
 <li>Adapter les services et les contrôleurs pour utiliser les DTO</li>
</ul>

**5. Création d'un service d'authentification séparé basé sur Spring Security et JWT :**
<ul>
  <li>Configurer Spring Security pour gérer l'authentification des utilisateurs</li>
 <li>Utiliser JWT (JSON Web Tokens) pour générer et valider les tokens d'authentification</li>
 <li>Implémenter les endpoints nécessaires pour l'inscription et la connexion des utilisateurs</li>
</ul>

**6. Sécurisation de l'application Digital Banking en utilisant Spring Security et JWT :**
<ul>
  <li>Appliquer les configurations de sécurité de Spring Security pour restreindre l'accès aux ressources de l'application</li>
 <li>Configurer les autorisations en fonction des rôles des utilisateurs</li>
 <li>Tester l'authentification et l'autorisation pour s'assurer du bon fonctionnement de la sécurité</li>
</ul>

**7. Création de la partie Frontend Web en utilisant Angular :**
<ul>
  <li>Concevoir et développer les composants Angular pour l'interface utilisateur de l'application</li>
 <li>Intégrer les appels API vers les endpoints REST du backend pour récupérer et modifier les données</li>
 <li>Implémenter les fonctionnalités de gestion des comptes bancaires, d'authentification et de sécurité</li>
  <li>Tester l'interface utilisateur et s'assurer de son bon fonctionnement.</li>
</ul>



  ## Architecture du projet
<div>
   <img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/architecture2.png" alt="image2"  width="30%">
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/architecture1.jpg" alt="image1" style="margin-top:1px;" width="45%">
  
</div>

### Stratégie adopter

>**Dans notre approche de gestion de l'héritage, une stratégie que nous avons utilisée est celle de la _"Single table"_. Avec cette approche, nous avons créé une seule table qui contient tous les attributs des trois classes concernées. Pour différencier les deux sous-types, nous avons ajouté une colonne spéciale appelée "colonne discriminante". Cette colonne nous permet de distinguer les instances appartenant aux différentes sous-classes au sein de la table unique.**

#### Couche DAO
> **Les entités JPA : Customer, BankAccount, Saving Account, CurrentAccount, AccountOperation**
 _Customer_
```java
@Entity
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
```java
@Entity
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
```java
@Entity
@DiscriminatorValue("Saving_Account")
@Data @NoArgsConstructor @AllArgsConstructor
public class SavingAccount extends BankAccount{
    private double interestRate;
}
```
_CurrentAccount_
```java
@Entity
@DiscriminatorValue("Current_Account")
@Data @NoArgsConstructor @AllArgsConstructor
public class CurrentAccount extends BankAccount{
    private double overDraft;

}
```
_AccountOperation_
```java
@Entity
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
> **les interfaces JPA Repository pour accéder aux données basées sur Spring Data**
_CustomerRepository_
```java
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer  c where c.name like :kw")
    List<Customer> searchCustomer(@Param("kw") String keyword);
}
```
_BankAccountRepository_
```java
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    List<BankAccount> getBankAccountByCustomer_Id(Long customerId);
}
```
_AccountOperationRepository_
```java
public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

    List<AccountOperation> findByBankAccountId(String accountId);

    Page<AccountOperation> findByBankAccountIdOrderByOperationDateDesc(String accountId, Pageable pageable);
}
```

> **Teste de la couche DAO**
```java
@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Ouassima", "Oualid", "Mohamed").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name +"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 9000);
                currentAccount.setCreatedDate(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 10000);
                savingAccount.setCreatedDate(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(4.3);
                bankAccountRepository.save(savingAccount);

            });

            bankAccountRepository.findAll().forEach(account -> {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 13000);
                    accountOperation.setOperationType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(account);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };

    }
```
>**Base de données**
```xml
spring.datasource.url=jdbc:mysql://localhost:3306/ebank-db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
spring.jpa.show-sql=false
server.port=8081
spring.main.allow-circular-references=true
```
<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/Customers.jpg" align="center" style="margin-top:1px;" width="40%"/>
<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/bank-accounts.jpg" align="center"  style="margin-top:1px;" width="60%"/>
<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/account-operations.jpg" align="center" style="margin-top:1px;" width="60%"/>


