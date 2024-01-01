package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Config.JwtAuthenticationFilter;
import com.FinalProject.NextGenFinalProject.Dto.AppResponse;
import com.FinalProject.NextGenFinalProject.Dto.CartRequest;
import com.FinalProject.NextGenFinalProject.Entity.Cart;
import com.FinalProject.NextGenFinalProject.Entity.CartItem;
import com.FinalProject.NextGenFinalProject.Entity.User;
import com.FinalProject.NextGenFinalProject.Entity.Product;
import com.FinalProject.NextGenFinalProject.Exception.ApiException;
import com.FinalProject.NextGenFinalProject.Repository.CartRepository;
import com.FinalProject.NextGenFinalProject.Repository.ProductRepository;
import com.FinalProject.NextGenFinalProject.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    public AppResponse<String> addToCart(CartRequest cartRequest) {
        String userEmail = JwtAuthenticationFilter.CURRENT_USER;

        Optional<User> userOptional = Optional.ofNullable(userRepo.findByEmail(userEmail).orElse(null));
        if (userOptional.isEmpty()) {
            throw new ApiException("User not found");
        }

        User user = userOptional.get();
        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
//            System.out.println("Cart is null");
            cart = new Cart();
            List<CartItem> cartItems = new ArrayList<>();

            for (CartRequest.ItemRequest itemRequest : cartRequest.getItemRequest()) {
                CartItem cartItem = new CartItem();
                Optional<Product> productOptional = productRepository.findById(itemRequest.getProductId());

                if (productOptional.isEmpty()) {
                    throw new ApiException("Product not found");
                }

                Product product = productOptional.get();
                cartItem.setProduct(product);
                cartItem.setQuantity(itemRequest.getQuantity());
                cartItem.setCart(cart);
                cartItems.add(cartItem);
            }

            cart.setCartItems(cartItems);
            cart.setUser(user);
            cartRepository.save(cart);

            return new AppResponse<>(200, "Added to Cart");
        } else {
            for (CartRequest.ItemRequest itemRequest : cartRequest.getItemRequest()) {
                Optional<Product> productOptional = productRepository.findById(itemRequest.getProductId());

                if (productOptional.isEmpty()) {
                    throw new ApiException("Product not found");
                }

                Product product = productOptional.get();
                updateCartItem(cart, product, itemRequest.getQuantity());
            }

            cartRepository.save(cart);
            return new AppResponse<>(200, "Updated Cart");
        }
    }

    private void updateCartItem(Cart cart, Product product, int quantity) {
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst().orElse(null);

        if (cartItem != null) {
            if (product.getIsAvailable()) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);
        }
    }

    @Transactional
    public AppResponse<String> removeFromCart(long cartItemId) {

        try {
            String userEmail = JwtAuthenticationFilter.CURRENT_USER;

            User user = userRepo.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiException("User not found"));
            Cart cart = cartRepository.findByUserId(user.getId());

            CartItem cartItemToRemove = cart.getCartItems().stream()
                    .filter(item -> item.getId().equals(cartItemId))
                    .findFirst()
                    .orElseThrow(() -> new ApiException("Product not found in cart"));

            cart.getCartItems().remove(cartItemToRemove);
            entityManager.remove(cartItemToRemove);
            cartRepository.save(cart);
            return new AppResponse<>("Product has been removed successfully");
        } catch (Exception e) {
            throw new ApiException("Failed to remove product from cart");
        }
    }


//
//    public AppResponse<String> removeFromCart(long cartItemId) {
//
//        String userEmail = JwtAuthenticationFilter.CURRENT_USER;
//
//        Optional<User> userOptional = Optional.ofNullable(userRepo.findByEmail(userEmail).orElse(null));
//        if (userOptional.isEmpty()) {
//            throw new ApiException("User not found");
//        }
//
//        User user = userOptional.get();
//        Cart cart = cartRepository.findByUserId(user.getId());
//
//        Optional<CartItem> cartItem = cart.getCartItems()
//                .stream()
//                .filter((item) -> item.getId().equals(cartItemId))
//                .findFirst();
//
//        if (cartItem.isEmpty()) {
//            return new AppResponse<>("No Such Product Found");
//        }
//
//        CartItem cartItemToRemove = cartItem.get();
//
//
//        cart.getCartItems().remove(cartItemToRemove);
//        cartRepository.save(cart);
//
//        return new AppResponse<>("Product has been removed successfully");
//
//    }
}
//    public AppResponse<CartResponseFromDb> viewCart(Long customerId) {
//        Cart cart = cartRepository.findByUserId(customerId);
//
//        if (cart == null) {
//            return new AppResponse<>("Cart is empty for the given customer", null);
//        }
//
//        CartResponseFromDb cartResponseFromDb = new CartResponseFromDb(cart);
//
//
//        return new AppResponse<>("Cart retrieved successfully", cartResponseFromDb);
//    }
//    public User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof User) {
//            return (User) authentication.getPrincipal();
//        } else {
//            return null;
//        }
//    }
//
//}
//
//
