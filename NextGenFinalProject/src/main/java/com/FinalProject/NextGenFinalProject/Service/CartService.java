package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Dto.AppResponse;
import com.FinalProject.NextGenFinalProject.Dto.CartResponseFromDb;
import com.FinalProject.NextGenFinalProject.Entity.Cart;
import com.FinalProject.NextGenFinalProject.Entity.CartItem;
import com.FinalProject.NextGenFinalProject.Entity.User;
import com.FinalProject.NextGenFinalProject.Entity.Product;
import com.FinalProject.NextGenFinalProject.Repository.CartRepository;
import com.FinalProject.NextGenFinalProject.Repository.ProductRepository;
import com.FinalProject.NextGenFinalProject.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepository productRepository;

    public AppResponse<String> addToCart(Long productId, Long customerId, int quantity) {
        User user = userRepo.findById(customerId).orElse(null);
        if (user == null) {
            return new AppResponse<String>("Please register with us");
        }
        Optional<Cart> checkCart = Optional.ofNullable(cartRepository.findByCustomerId(customerId));

        if (checkCart.isEmpty()) {

            Cart createCart = new Cart(user);

            Product product = productRepository.findById(productId).orElse(null);

            if (product == null) {
                return new AppResponse<>("Product not found");
            }

            CartItem cartItem = createCart.getCartItems().stream()
                    .filter((item) -> item.getProduct().equals(product))
                    .findFirst().orElse(null);

            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                cartItem = new CartItem();
                cartItem.setCart(createCart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                createCart.getCartItems().add(cartItem);
            }

            // Update the modification date before saving
            createCart.setModificationDate(LocalDateTime.now());

            // Save the cart to the database
            cartRepository.save(createCart);
//

        }
        return new AppResponse<>("Added to Cart");
    }




    public AppResponse<String> removeFromCart(Long productId, Long customerId){

        Cart cart= cartRepository.findByCustomerId(customerId);

        Product product = productRepository.findById(productId).orElse(null);


        Optional<CartItem> cartItem=  cart.getCartItems()
                   .stream()
                   .filter((item)-> item.getProduct().equals(product))
                    .findFirst();

        if (cartItem.isEmpty()) {
            return new AppResponse<>("No Such Product Found");
        }

        CartItem cartItemToRemove = cartItem.get();


        cart.getCartItems().remove(cartItemToRemove);
        cartRepository.save(cart);

           return new AppResponse<>("Product has been removed successfully");

    }

    public AppResponse<CartResponseFromDb> viewCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId);

        if (cart == null) {
            return new AppResponse<>("Cart is empty for the given customer", null);
        }

        CartResponseFromDb cartResponseFromDb = new CartResponseFromDb(cart);


        return new AppResponse<>("Cart retrieved successfully", cartResponseFromDb);
    }

    }


