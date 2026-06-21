package com.bablumia.store.carts;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bablumia.store.products.ProductNotFoundException;
import com.bablumia.store.products.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart(CartDto cartDto) {
        var cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartItemDto addItemToCart(UUID cartId, Long ProductId) {

        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        System.out.println(cart);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        var product = productRepository.findById(ProductId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId, int quantity) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new ProductNotFoundException();
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeItemFromCart(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        cart.removeItem(productId);

        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        cart.clear();

        cartRepository.save(cart);
    }
}
