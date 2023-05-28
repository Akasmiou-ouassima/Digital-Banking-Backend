package com.akasmiou.ouassima.EBanking.dtos;

import com.akasmiou.ouassima.EBanking.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class SavingBankAccountDTO extends BankAccountDTO{

    private String id;
    private double balance;
    private Date createdDate;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}
