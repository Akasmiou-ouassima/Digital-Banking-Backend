package com.springangular.ebankingbackend.dtos;

import lombok.Data;

@Data
public class TransferRequestDTO {
    private String accountSource;
    private String accountTarget;
    private double amount;
    private String description;

}
