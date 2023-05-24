package com.springangular.ebankingbackend.dtos;

import lombok.Data;
import jakarta.persistence.*;

@Data
public class CustomerDTO {

    private Long id;
    private String name;
    private String email;
}
