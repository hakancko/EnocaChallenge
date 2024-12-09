package com.example.commercemanager.service;
import com.example.commercemanager.dto.CartDTO;
import com.example.commercemanager.entity.*;
import com.example.commercemanager.repository.*;
import com.example.commercemanager.exception.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductService productService, CustomerService customerService, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public CartDTO getCart(Long customerId) {
        CartDTO cartDTO = modelMapper.map(cartRepository.findByCustomerId(customerId), CartDTO.class);
        return cartDTO;
    }

    @Transactional
    public Cart addProductToCart(Long customerId, Long productId, int quantity) {

        Customer customer = customerService.getCustomerById(customerId);
        Product product = productService.getProductById(productId);


        if (!productService.checkProductAvailability(productId, quantity)) {
            throw new InsufficientStockException("Yeterli stok bulunmamaktadır");
        }

        Cart cart = customer.getCart();
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (cartItem == null) {

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setItemTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        } else {

            int totalQuantity = cartItem.getQuantity() + quantity;
            if (!productService.checkProductAvailability(productId, quantity)) {
                throw new InsufficientStockException("Yeterli stok bulunmamaktadır");
            }
            cartItem.setQuantity(totalQuantity);
            cartItem.setItemTotalPrice(
                    product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }


        productService.updateProductStock(productId, -quantity);


        cartItemRepository.save(cartItem);

        updateCartTotalPrice(cart);

        return cart;
    }

    @Transactional
    public Cart removeProductFromCart(Long customerId, Long productId, int quantity) {
        Customer customer = customerService.getCustomerById(customerId);
        Product product = productService.getProductById(productId);
        Cart cart = customer.getCart();

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (cartItem == null) {
            throw new CartItemNotFoundException("Sepette böyle bir ürün bulunamadı");
        }

        if (cartItem.getQuantity() <= quantity) {
            cartItemRepository.delete(cartItem);
            productService.updateProductStock(productId, cartItem.getQuantity());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
            cartItem.setItemTotalPrice(
                    product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
            cartItemRepository.save(cartItem);
            productService.updateProductStock(productId, quantity);
        }

        updateCartTotalPrice(cart);

        return cart;
    }

    @Transactional
    public void updateCartTotalPrice(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(CartItem::getItemTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
    }

    @Transactional
    public void emptyCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId);

        for (CartItem item : cart.getCartItems()) {
            productService.updateProductStock(item.getProduct().getId(), item.getQuantity());
        }

        cartItemRepository.deleteAll(cart.getCartItems());

        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }
}