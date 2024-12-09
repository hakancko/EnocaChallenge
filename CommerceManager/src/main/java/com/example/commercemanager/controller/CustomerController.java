package com.example.commercemanager.controller;

import com.example.commercemanager.dto.CustomerDTO;
import com.example.commercemanager.entity.Customer;
import com.example.commercemanager.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    // Manuel constructor
    public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customerDTO) {

        Customer customer = modelMapper.map(customerDTO, Customer.class);
        Customer savedCustomer = customerService.addCustomer(customer);
        CustomerDTO responseDTO = modelMapper.map(savedCustomer, CustomerDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        CustomerDTO responseDTO = modelMapper.map(customer, CustomerDTO.class);
        return ResponseEntity.ok(responseDTO);
    }
}