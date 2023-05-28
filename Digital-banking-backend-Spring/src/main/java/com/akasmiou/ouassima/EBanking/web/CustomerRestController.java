package com.akasmiou.ouassima.EBanking.web;


import com.akasmiou.ouassima.EBanking.dtos.CustomerDTO;
import com.akasmiou.ouassima.EBanking.exceptions.CustomerNotFoundException;
import com.akasmiou.ouassima.EBanking.services.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@CrossOrigin("*") // accepter tous les domaines
public class CustomerRestController {
    private BankAccountService bankAccountService;

    public CustomerRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        log.info("Customers have been searched");
        return bankAccountService.searchCustomers("%" + keyword + "%");
    }
    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/customers")
    public List<CustomerDTO> customers() {
        log.info("Customers' list have been returned");
        return bankAccountService.listCustomer();
    }
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        CustomerDTO customerDTO = bankAccountService.getCustomer(customerId);
        return customerDTO;
    }
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable(name = "customerId") Long customerId, @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }
    @PostAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        bankAccountService.deleteCustomer(id);
    }
}
