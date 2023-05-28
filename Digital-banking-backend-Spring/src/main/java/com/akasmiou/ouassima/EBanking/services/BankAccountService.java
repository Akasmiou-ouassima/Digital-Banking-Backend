package com.akasmiou.ouassima.EBanking.services;


import com.akasmiou.ouassima.EBanking.dtos.*;
import com.akasmiou.ouassima.EBanking.enums.AccountStatus;
import com.akasmiou.ouassima.EBanking.exceptions.BalanceNotSufficientException;
import com.akasmiou.ouassima.EBanking.exceptions.BankAccountNotFoundException;
import com.akasmiou.ouassima.EBanking.exceptions.CustomerNotFoundException;

import java.util.List;

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
