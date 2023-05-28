package com.akasmiou.ouassima.EBanking.dtos;

import com.akasmiou.ouassima.EBanking.enums.OperationType;
import lombok.Data;

import java.util.Date;

@Data
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType operationType;
    private String description;
}
