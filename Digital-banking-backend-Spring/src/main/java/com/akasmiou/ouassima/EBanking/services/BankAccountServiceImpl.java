package com.akasmiou.ouassima.EBanking.services;

import com.akasmiou.ouassima.EBanking.dtos.*;
import com.akasmiou.ouassima.EBanking.entities.*;
import com.akasmiou.ouassima.EBanking.enums.AccountStatus;
import com.akasmiou.ouassima.EBanking.enums.OperationType;
import com.akasmiou.ouassima.EBanking.exceptions.BalanceNotSufficientException;
import com.akasmiou.ouassima.EBanking.exceptions.BankAccountNotFoundException;
import com.akasmiou.ouassima.EBanking.exceptions.CustomerNotFoundException;
import com.akasmiou.ouassima.EBanking.mappers.BankAccountMapperImpl;
import com.akasmiou.ouassima.EBanking.repositories.AccountOperationRepository;
import com.akasmiou.ouassima.EBanking.repositories.BankAccountRepository;
import com.akasmiou.ouassima.EBanking.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository,
                                  CustomerRepository customerRepository,
                                  AccountOperationRepository accountOperationRepository, BankAccountMapperImpl dtoMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.dtoMapper = dtoMapper;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountServiceImpl.class); // (this.getClass.getName())

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        LOGGER.info("Saving new Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedDate(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setStatus(AccountStatus.CREATED);
        CurrentAccount currentAccountSaved = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(currentAccountSaved);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedDate(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setStatus(AccountStatus.CREATED);
        SavingAccount savingAccountSaved = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savingAccountSaved);
    }

    @Override
    public BankAccountDTO updateBankAccount(String accountId, AccountStatus accountStatus) throws BankAccountNotFoundException {

        BankAccount bankAccount = getBankAccountById(accountId);

        if(bankAccount instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                    currentAccount.setStatus(accountStatus);
            CurrentAccount updatedCurrentAccount = bankAccountRepository.save(currentAccount);
            return dtoMapper.fromCurrentBankAccount(updatedCurrentAccount);
        } else {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            savingAccount.setStatus(accountStatus);
            SavingAccount updatedSaveAccount = bankAccountRepository.save(savingAccount);
            return dtoMapper.fromSavingBankAccount(updatedSaveAccount);
        }
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String id) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("No Bank Account with this id: " + id ));
        if(bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(
            String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {

        BankAccount bankAccount = getBankAccountById(accountId);

        if(bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }
        AccountOperation debitAccountOperation = new AccountOperation();
        debitAccountOperation.setOperationType(OperationType.DEBIT);
        debitAccountOperation.setAmount(amount);
        debitAccountOperation.setOperationDate(new Date());
        debitAccountOperation.setBankAccount(bankAccount);
        debitAccountOperation.setDescription(description);
        accountOperationRepository.save(debitAccountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit (
            String accountId, double amount, String description) throws BankAccountNotFoundException {

        BankAccount bankAccount = getBankAccountById(accountId);

        AccountOperation creditAccountOperation = new AccountOperation();
        creditAccountOperation.setOperationType(OperationType.CREDIT);
        creditAccountOperation.setAmount(amount);
        creditAccountOperation.setOperationDate(new Date());
        creditAccountOperation.setBankAccount(bankAccount);
        creditAccountOperation.setDescription(description);
        accountOperationRepository.save(creditAccountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer (
            String accountIdSource, String accountIdDestination, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, description);
        credit(accountIdDestination, amount, description);
    }

    @Override
    public List<BankAccountDTO> getListBankAccounts() {
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccountList.stream().map(bankAccount -> {
            if(bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        log.info("All Bank Accounts are loading successfully");
        return bankAccountDTOS;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with this id : " + customerId));
        return dtoMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        LOGGER.info("Updating a Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
        customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("Customer not found with this id: " + customerId));
        customerRepository.deleteById(customerId);
    }

    private BankAccount getBankAccountById(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("No Bank Account with this id: " + accountId ));
        return bankAccount;
    }
    @Override
    public List<AccountOperationDTO> getAccountHistoryByList(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        if(accountOperations == null ) {
            return null;
        }
        if(accountOperations.isEmpty()) {
            return new ArrayList<>();
        }
       List<AccountOperationDTO> accountOperationDTOS = accountOperations.stream().map(operation ->
            dtoMapper.fromAccountOperation(operation)).collect(Collectors.toList());

        return accountOperationDTOS;
    }
    @Override
    public List<BankAccountDTO> getBankAccountsByCustomerId(Long customerId) {
        List<BankAccount> bankAccountList = bankAccountRepository.getBankAccountByCustomer_Id(customerId);
        List<BankAccountDTO> bankAccountDTOS;

        bankAccountDTOS = bankAccountList.stream().map(bankAccount -> {
            if(bankAccount instanceof CurrentAccount) {
               return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
            } else {
            return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);
            }
        }).collect(Collectors.toList());

        return bankAccountDTOS;
    }

    @Override
    public AccountHistoryDTO getAccountHistoryByPage(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountById(accountId);



        Page<AccountOperation> accountOperations= accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();

        List<AccountOperationDTO> accountOperationDTOS =
                accountOperations.getContent().stream().map(operation -> dtoMapper.fromAccountOperation(operation)).collect(Collectors.toList());

        accountHistoryDTO.setAccountOperationDTOList(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());

        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.searchCustomer(keyword);
        List<CustomerDTO> customerDTOS =
                customers.stream().map(customer ->
            dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTOS;
    }

}
