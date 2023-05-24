package com.springangular.ebankingbackend.web;

import com.springangular.ebankingbackend.dtos.*;
import com.springangular.ebankingbackend.enums.AccountStatus;
import com.springangular.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.springangular.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.springangular.ebankingbackend.exceptions.CustomerNotFoundException;
import com.springangular.ebankingbackend.services.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin("*")
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    public BankAccountRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountNotFoundException {
        log.info("BankAccountDTO is returned");
        return bankAccountService.getBankAccount(id);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> getListBankAccounts() {
        return bankAccountService.getListBankAccounts();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistoryByList(@PathVariable String accountId) {
        return bankAccountService.getAccountHistoryByList(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistoryByPage(
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {


       return bankAccountService.getAccountHistoryByPage(accountId, page, size);
    }

    @PostMapping("/customers/{customerId}/current-accounts")
    public CurrentBankAccountDTO saveCurrentBankAccount(
            @RequestParam double initialBalance,
            @RequestParam double overDraft,
            @PathVariable Long customerId) throws CustomerNotFoundException {
        log.info("A Current Account has been successfully");
        return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
    }

    @PostMapping("/customers/{customerId}/saving-accounts")
    public SavingBankAccountDTO saveSavingBankAccount(
            @RequestParam double initialBalance,
            @RequestParam double interestRate,
            @PathVariable Long customerId) throws CustomerNotFoundException {
        log.info("A Saving Account has been successfully");

        return bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
    }

    @PutMapping("/accounts/{accountId}")
    public BankAccountDTO updateBankAccount(@PathVariable String accountId, @RequestParam AccountStatus accountStatus) throws BankAccountNotFoundException {
        log.info("Update a bank account return with the account type");
        return bankAccountService.updateBankAccount(accountId, accountStatus);
    }
    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        log.info("Debit is successful");
        bankAccountService.debit(
                debitDTO.getAccountId(),
                debitDTO.getAmount(),
                debitDTO.getDescription());

        return debitDTO;
    }

    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        log.info("Credit is successful");
        bankAccountService.debit(
                creditDTO.getAccountId(),
                creditDTO.getAmount(),
                creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        log.info("Transfer is successful between theses bankAccounts sourceId: "
                + transferRequestDTO.getAccountSource() + " and targetId: " + transferRequestDTO.getAccountTarget());
        bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountTarget(),
                transferRequestDTO.getAmount(),
                transferRequestDTO.getDescription());
    }
    @GetMapping("/customers/{customerId}/accounts")
    public List<BankAccountDTO> getBankAccountsByCustomerId(@PathVariable Long customerId) {
        List<BankAccountDTO> bankAccountDTOS = bankAccountService.getBankAccountsByCustomerId(customerId);
        return bankAccountDTOS;
    }

}
