package com.akasmiou.ouassima.EBanking.web;

import com.akasmiou.ouassima.EBanking.dtos.*;
import com.akasmiou.ouassima.EBanking.enums.AccountStatus;
import com.akasmiou.ouassima.EBanking.exceptions.BalanceNotSufficientException;
import com.akasmiou.ouassima.EBanking.exceptions.BankAccountNotFoundException;
import com.akasmiou.ouassima.EBanking.exceptions.CustomerNotFoundException;
import com.akasmiou.ouassima.EBanking.services.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
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
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping("/accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountNotFoundException {
        log.info("BankAccountDTO is returned");
        return bankAccountService.getBankAccount(id);
    }
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/accounts")
    public List<BankAccountDTO> getListBankAccounts() {
        return bankAccountService.getListBankAccounts();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistoryByList(@PathVariable String accountId) {
        return bankAccountService.getAccountHistoryByList(accountId);
    }
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
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
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PutMapping("/accounts/{accountId}")
    public BankAccountDTO updateBankAccount(@PathVariable String accountId, @RequestParam AccountStatus accountStatus) throws BankAccountNotFoundException {
        log.info("Update a bank account return with the account type");
        return bankAccountService.updateBankAccount(accountId, accountStatus);
    }
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        log.info("Debit is successful");
        bankAccountService.debit(
                debitDTO.getAccountId(),
                debitDTO.getAmount(),
                debitDTO.getDescription());

        return debitDTO;
    }
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        log.info("Credit is successful");
        bankAccountService.debit(
                creditDTO.getAccountId(),
                creditDTO.getAmount(),
                creditDTO.getDescription());
        return creditDTO;
    }
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(transferRequestDTO.getAccountTarget(), transferRequestDTO.getAccountSource(), transferRequestDTO.getAmount());
    }
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping("/customers/{customerId}/accounts")
    public List<BankAccountDTO> getBankAccountsByCustomerId(@PathVariable Long customerId) {
        List<BankAccountDTO> bankAccountDTOS = bankAccountService.getBankAccountsByCustomerId(customerId);
        return bankAccountDTOS;
    }

}
