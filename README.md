<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/icon.png" align="right" />

 ## 🔗  Digital-Banking-Project


>**Le travail à effectuer consiste à développer une application Web basée sur Spring et Angular pour la gestion de comptes bancaires.**
 
 **Les technologies suivantes seront utilisées dans ce dépôt :**
- [x] Backend : Spring
- [x] SGBD : MySQL

## Diagramme de cas d'utilisation du projet Digital banking
<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/Diagramme%20de%20classe%20digital%20banking.jpg">

**1. Création et test de la couche DAO :**
<ul>
  <li>Définir les entités JPA </li>
 <li>Créer les interfaces JPA Repository basées sur Spring Data </li>
 <li>Tester la couche DAO</li>
</ul>

**2. Création et test de la couche service en utilisant les DTO :**
<ul>
   <li>Créer des objets DTO (Data Transfer Objects) pour transférer les données entre les couches</li>
  <li>Définir les opérations du service pour ajouter des comptes, ajouter des clients, effectuer des débits, des crédits et des virements, et consulter un compte
.</li>
 <li>Implémenter la logique métier pour ces opérations </li>
  <li>Tester les opérations CRUD (Create, Read, Update, Delete) pour les entités </li>
</ul>

**3. Création et test de la couche Web (Rest Controller) en utilisant les DTO  :**
<ul>
  <li>Créer les contrôleurs REST pour exposer les fonctionnalités de l'application</li>
 <li>Définir les endpoints REST pour les opérations du service</li>
 <li>Tester les endpoints à l'aide d'outils comme Swagger</li>
</ul>

**4. Création d'un service d'authentification séparé basé sur Spring Security et JWT :**
<ul>
  <li>Configurer Spring Security pour gérer l'authentification des utilisateurs</li>
 <li>Utiliser JWT (JSON Web Tokens) pour générer et valider les tokens d'authentification</li>
 <li>Implémenter les endpoints nécessaires pour l'inscription et la connexion des utilisateurs</li>
</ul>

**5. Sécurisation de l'application Digital Banking en utilisant Spring Security et JWT :**
<ul>
  <li>Appliquer les configurations de sécurité de Spring Security pour restreindre l'accès aux ressources de l'application</li>
 <li>Configurer les autorisations en fonction des rôles des utilisateurs</li>
 <li>Tester l'authentification et l'autorisation pour s'assurer du bon fonctionnement de la sécurité</li>
</ul>


  ## Architecture du projet
<div>
   <img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/architecture2.png" alt="image2"  width="30%">
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Project/blob/main/Les%20images/architecture1.jpg" alt="image1" style="margin-top:1px;" width="45%">
  
</div>

### Stratégie à adopter

>**Dans notre approche de gestion de l'héritage, une stratégie que nous avons utilisée est celle de la _"Single table"_. Avec cette approche, nous avons créé une seule table qui contient tous les attributs des trois classes concernées. Pour différencier les deux sous-types, nous avons ajouté une colonne spéciale appelée "colonne discriminante". Cette colonne nous permet de distinguer les instances appartenant aux différentes sous-classes au sein de la table unique.**

### Couche DAO
> **Les entités JPA : Customer, BankAccount, Saving Account, CurrentAccount, AccountOperation**
 _**Customer**_
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
 _**BankAccount**_
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
_**Saving Account**_
```java
@Entity
@DiscriminatorValue("Saving_Account")
@Data @NoArgsConstructor @AllArgsConstructor
public class SavingAccount extends BankAccount{
    private double interestRate;
}
```
_**CurrentAccount**_
```java
@Entity
@DiscriminatorValue("Current_Account")
@Data @NoArgsConstructor @AllArgsConstructor
public class CurrentAccount extends BankAccount{
    private double overDraft;

}
```
_**AccountOperation**_
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
_**CustomerRepository**_
```java
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer  c where c.name like :kw")
    List<Customer> searchCustomer(@Param("kw") String keyword);
}
```
_**BankAccountRepository**_
```java
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    List<BankAccount> getBankAccountByCustomer_Id(Long customerId);
}
```
_**AccountOperationRepository**_
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


### Couche Service

<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Backend/blob/main/Les%20images/services.jpg"  />

> **Création des objets DTO**

<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Backend/blob/main/Les%20images/Dtos.jpg" />

_**Customer DTO**_,  _**Bank Account DTO**_, _**Cusrrent Account DTO**_, _**Saving Account DTO**_, _**Account OPerations DTO**_, _**Account History DTO**_
    <img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Backend/blob/main/Les%20images/dtos1.jpg" />
    
_**Operations DTOS**_
 <img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Backend/blob/main/Les%20images/operationsdto.jpg" />
 


> **Création des mappers**

```java
@Service
@Transactional
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);

        return customer;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount (CurrentAccount currentAccount) {
        CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentBankAccountDTO);

        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());

        return currentBankAccountDTO;
    }
    public CurrentAccount fromCurrentAccountDTO (CurrentBankAccountDTO currentBankAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO, currentAccount);

        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public SavingBankAccountDTO fromSavingBankAccount (SavingAccount savingAccount) {
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount, savingBankAccountDTO);

        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }
    public SavingAccount fromSavingBankAccountDTO (SavingBankAccountDTO savingBankAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();

        BeanUtils.copyProperties(savingBankAccountDTO, savingAccount);

        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));

        return savingAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);

        return accountOperationDTO;
    }
}
 ```
> **Définition les opérations du service**

_**Interface BankAccountService**_

```java
public interface BankAccountService {

  CustomerDTO saveCustomer(CustomerDTO customerDTO);

  CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
  SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

  BankAccountDTO updateBankAccount(String accountId, AccountStatus accountStatus) throws BankAccountNotFoundException;

  List<CustomerDTO> listCustomer();

  BankAccountDTO getBankAccount(String id) throws BankAccountNotFoundException;

  void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
  void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

  void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

  List<BankAccountDTO> getListBankAccounts();

  CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

  CustomerDTO updateCustomer(CustomerDTO customerDTO);

  void deleteCustomer(Long customerId) throws CustomerNotFoundException;

  List<AccountOperationDTO> getAccountHistoryByList(String accountId);

  List<BankAccountDTO> getBankAccountsByCustomerId(Long customerId);

  AccountHistoryDTO getAccountHistoryByPage(String accountId, int page, int size) throws BankAccountNotFoundException;

  List<CustomerDTO> searchCustomers(String keyword);
}
```
🔗[➤ **Implémentation de la logique métier pour ces opérations** ](https://github.com/Akasmiou-ouassima/Digital-Banking-Backend/blob/main/Digital-banking-backend-Spring/src/main/java/com/akasmiou/ouassima/EBanking/services/BankAccountServiceImpl.java)

>_**Tester les opérations CRUD**_

```java
@Bean
    CommandLineRunner start (AccountService accountService) {
        return args -> {
            Stream.of("Ouassima", "Mohamed", "Jinan", "Oualid").forEach(name -> {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setName(name);
                customerDTO.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customerDTO);
            });
            bankAccountService.listCustomer().forEach( customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 85000, 3.2, customer.getId());
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

            try {
                List<BankAccountDTO> bankAccountList = bankAccountService.getListBankAccounts();
                for (BankAccountDTO bankAccount : bankAccountList) {
                    for (int i = 0; i < 10; i++) {
                        String accountId;
                        if(bankAccount instanceof SavingBankAccountDTO) {
                            accountId = ((SavingBankAccountDTO) bankAccount).getId();
                        } else {
                            accountId = ((CurrentBankAccountDTO) bankAccount).getId();
                        }
                        bankAccountService.credit(
                                accountId,
                                10000 + Math.random() * 120000,
                                "Credit");

                        bankAccountService.debit(
                                accountId,
                                1000 + Math.random() * 9000,
                                "Debit");

                    }
                }
            } catch (BalanceNotSufficientException | BankAccountNotFoundException e) {
                e.printStackTrace();
            }
        };
    }
   ```
   
> **Teste les endpoints à l'aide d'outils comme Swagger**

```java
L'interface de test de notre API, générée par Swagger, est accessible via http://localhost:8081/swagger-ui/index.html
```
<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Backend/blob/main/Les%20images/swagger1.jpg" />


**_Teste de la méthode GET Customers_**

<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Backend/blob/main/Les%20images/getcustomers-swagger.jpg" />

**_Teste de la méthode GET Customer By Id_**
<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Backend/blob/main/Les%20images/getcustomerbyid.jpg" />

**_Teste de la méthode GET Accounts_**
<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Backend/blob/main/Les%20images/getaccounts.jpg" />

### Couche Security

>La classe SecurityConfig configure la sécurité dans une application Java avec Spring Security. Les principales étapes incluent la désactivation de CSRF, la configuration de la politique >de sessions, la gestion de CORS, l'autorisation d'accès à certaines URL et l'ajout de filtres JWT personnalisés pour l'authentification et l'autorisation.

> **_les configurations de sécurité de Spring Security_**

<img src="https://github.com/Akasmiou-ouassima/Digital-Banking-Backend/blob/main/Les%20images/security-config.png" width="40%"/>

🔗 [➤ **Security JWT** ](https://github.com/Akasmiou-ouassima/Digital-Banking-Backend/tree/main/Digital-banking-backend-Spring/src/main/java/com/akasmiou/ouassima/EBanking/security)
