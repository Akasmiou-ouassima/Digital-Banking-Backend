package com.akasmiou.ouassima.EBanking;


import com.akasmiou.ouassima.EBanking.dtos.BankAccountDTO;
import com.akasmiou.ouassima.EBanking.dtos.CurrentBankAccountDTO;
import com.akasmiou.ouassima.EBanking.dtos.CustomerDTO;
import com.akasmiou.ouassima.EBanking.dtos.SavingBankAccountDTO;
import com.akasmiou.ouassima.EBanking.entities.AccountOperation;
import com.akasmiou.ouassima.EBanking.entities.CurrentAccount;
import com.akasmiou.ouassima.EBanking.entities.Customer;
import com.akasmiou.ouassima.EBanking.entities.SavingAccount;
import com.akasmiou.ouassima.EBanking.enums.AccountStatus;
import com.akasmiou.ouassima.EBanking.enums.OperationType;
import com.akasmiou.ouassima.EBanking.exceptions.BalanceNotSufficientException;
import com.akasmiou.ouassima.EBanking.exceptions.BankAccountNotFoundException;
import com.akasmiou.ouassima.EBanking.exceptions.CustomerNotFoundException;
import com.akasmiou.ouassima.EBanking.repositories.AccountOperationRepository;
import com.akasmiou.ouassima.EBanking.repositories.BankAccountRepository;
import com.akasmiou.ouassima.EBanking.repositories.CustomerRepository;
import com.akasmiou.ouassima.EBanking.security.entities.AppRole;
import com.akasmiou.ouassima.EBanking.security.entities.AppUser;
import com.akasmiou.ouassima.EBanking.security.services.AccountService;
import com.akasmiou.ouassima.EBanking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.Date;


import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


@SpringBootApplication
public class EbankingBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
    @Bean
    CommandLineRunner start (BankAccountService bankAccountService, AccountService accountService) {
        return args -> {
            accountService.addNewRole(new AppRole(null, "ADMIN"));
            accountService.addNewRole(new AppRole(null, "CUSTOMER"));

            accountService.addNewUser(new AppUser(null, "ouassima", "1234", new ArrayList<>()));
            accountService.addNewUser(new AppUser(null, "admin", "1234", new ArrayList<>()));
            accountService.addNewUser(new AppUser(null, "mohamed", "1234", new ArrayList<>()));

            accountService.addRoleToUser("ouassima", "CUSTOMER");
            accountService.addRoleToUser("admin", "ADMIN");
            accountService.addRoleToUser("mohamed", "CUSTOMER");


            Stream.of("Polat", "Ibrahim", "Gulqiz", "Aygul").forEach(name -> {
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
                                "First Credit");

                        bankAccountService.debit(
                                accountId,
                                1000 + Math.random() * 9000,
                                "First Debit");

                    }
                }
            } catch (BalanceNotSufficientException | BankAccountNotFoundException e) {
                e.printStackTrace();
            }
        };
    }


    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Alim", "Memet", "Guzel").forEach(name -> {
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
                currentAccount.setOverDraft(8000);
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
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setOperationType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(account);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };

    }



    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


