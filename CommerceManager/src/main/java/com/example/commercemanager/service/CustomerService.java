package com.example.commercemanager.service;

import com.example.commercemanager.entity.Cart;
import com.example.commercemanager.entity.Customer;
import com.example.commercemanager.exception.CustomerAlreadyExistsException;
import com.example.commercemanager.exception.CustomerNotFoundException;
import com.example.commercemanager.repository.CartRepository;
import com.example.commercemanager.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    public CustomerService(CartRepository cartRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    /*@Transactional*/
    public Customer addCustomer(Customer customerDTO) {
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new CustomerAlreadyExistsException("Bu e-posta ile zaten bir müşteri mevcut.");
        }

        Customer customer = modelMapper.map(customerDTO, Customer.class);
        Customer savedCustomer = customerRepository.save(customer);

        Cart cart = new Cart();
        cart.setCustomer(savedCustomer);
        cartRepository.save(cart);

        savedCustomer.setCart(cart);
        return savedCustomer;
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Müşteri bulunamadı."));
    }

    public Customer getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new CustomerNotFoundException("Müşteri bulunamadı.");
        }
        return customer;
    }
}
