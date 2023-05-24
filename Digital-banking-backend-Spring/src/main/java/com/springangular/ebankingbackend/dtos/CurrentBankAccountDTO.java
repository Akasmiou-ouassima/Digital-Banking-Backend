package com.springangular.ebankingbackend.dtos;

import com.springangular.ebankingbackend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class CurrentBankAccountDTO extends BankAccountDTO{

    private String id; // RIB
    private double balance;
    private Date createdDate;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft; // découvert

}
