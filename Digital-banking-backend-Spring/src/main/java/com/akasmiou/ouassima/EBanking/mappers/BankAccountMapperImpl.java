package com.akasmiou.ouassima.EBanking.mappers;


import com.akasmiou.ouassima.EBanking.dtos.AccountOperationDTO;
import com.akasmiou.ouassima.EBanking.dtos.CurrentBankAccountDTO;
import com.akasmiou.ouassima.EBanking.dtos.CustomerDTO;
import com.akasmiou.ouassima.EBanking.dtos.SavingBankAccountDTO;
import com.akasmiou.ouassima.EBanking.entities.AccountOperation;
import com.akasmiou.ouassima.EBanking.entities.CurrentAccount;
import com.akasmiou.ouassima.EBanking.entities.Customer;
import com.akasmiou.ouassima.EBanking.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



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
