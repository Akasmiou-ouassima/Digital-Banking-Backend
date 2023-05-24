package com.springangular.ebankingbackend;

import com.springangular.ebankingbackend.Security.entities.AppUser;
import com.springangular.ebankingbackend.Security.service.AccountService;
import com.springangular.ebankingbackend.dtos.BankAccountDTO;
import com.springangular.ebankingbackend.dtos.CurrentBankAccountDTO;
import com.springangular.ebankingbackend.dtos.CustomerDTO;
import com.springangular.ebankingbackend.dtos.SavingBankAccountDTO;
import com.springangular.ebankingbackend.entities.*;
import com.springangular.ebankingbackend.enums.AccountStatus;
import com.springangular.ebankingbackend.enums.OperationType;
import com.springangular.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.springangular.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.springangular.ebankingbackend.exceptions.CustomerNotFoundException;
import com.springangular.ebankingbackend.repositories.AccountOperationRepository;
import com.springangular.ebankingbackend.repositories.BankAccountRepository;
import com.springangular.ebankingbackend.repositories.CustomerRepository;
import com.springangular.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
@Configuration
public class EbankingBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}
	//@Bean
	CommandLineRunner start (BankAccountService bankAccountService) {
		return args -> {
			Stream.of("Polat", "Ibrahim", "Gulqiz", "Aygul").forEach(name -> {
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setName(name);
				customerDTO.setEmail(name + "@gmail.com");
				bankAccountService.saveCustomer(customerDTO);
			});
			// 4 tours au total
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
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner start(AccountService accountService){
		return args -> {
			accountService.addNewUser(new AppUser(null, "AKASMIOU ouassima", "1234", "ADMIN"));
		};
	}
}
