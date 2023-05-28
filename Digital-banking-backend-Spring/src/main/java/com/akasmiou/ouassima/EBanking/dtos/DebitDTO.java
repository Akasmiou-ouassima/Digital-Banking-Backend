package com.akasmiou.ouassima.EBanking.dtos;

import lombok.Data;

@Data
public class DebitDTO {
    private String accountId;
    private double amount;
    private String description;


}
